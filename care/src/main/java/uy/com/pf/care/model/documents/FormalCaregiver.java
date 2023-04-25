package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.FormalCaregiverObject;
import uy.com.pf.care.model.objects.InterestZonesObject;

import java.util.*;

@Document("FormalCaregivers")
@CompoundIndexes({
        @CompoundIndex(def = "{'mail':1}", unique = true),
        @CompoundIndex(def = "{'countryName':1, 'name':1, 'telephone':1}", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FormalCaregiver extends FormalCaregiverObject {
    @Id
    private String formalCaregiverId;
    private String telephone;
    private String mail;
    private String comments;

    /* Votos por cada puntaje.
       votes[0..4], donde: votes[n] es la cantidad de votos para el puntaje "n", con n=[0, 4].
            votes[0]: cantidad de votos para el puntaje 1
            ...
            votes[4]: cantidad de votos para el puntaje 5
     */
    private int[] votes;
    @Transient
    private double averageScore;

    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente
    private Boolean deleted;

    /* Zonas de interés del Cuidador Formal:
         -Si interestZones=[], implica que llega a todos los Departamentos/Provincias del pais.
         -Si interestZones[n] (es un departmentName registrado), con cities=[], implica que llega al departamento
            entero.
         -Si interestZones[n] tiene un departmentName con una cityName registrados, con neighborhoodNames=[], implica
            que llega a toda la ciudad/localidad de dicho departamento.
         -Si interestZones[n] tiene un departmentName, una cityName y barrios registrados, implica que llega solo a esos
         barrios de la ciudad.
    */
    List<InterestZonesObject> interestZones = new ArrayList<>();
    private String countryName; // Pais de residencia del Cuidador Formal

    /*  Si previousScore = -1, implica que no hay un puntaje previo asignado.
        Si previousScore = [1..5], implica hay un puntaje previo en el ordinal previousScore - 1 y debe restarse.
        currentScore: es el puntaje que debe sumarse en el ordinal currentScore - 1.
     */
    public void updateVote(int previousScore, int currentScore){
        if (previousScore != -1)
            votes[previousScore-1] = votes[previousScore-1] - 1;
        votes[currentScore-1] = votes[currentScore-1] + 1;
    }

    public double getAverageScore(){
        int votesCount = 0, votesByScore = 0;
        double votesSum = 0;
        for (int score = 1; score <= 5; score++) {
            votesByScore = votes[score-1];
            votesCount += votesByScore;
            votesSum += votesByScore * score;
        }
        return votesCount > 0 ? votesSum / votesCount : 0;
    }
}
