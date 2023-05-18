package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jdk.jfr.BooleanFlag;
import lombok.*;
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
        //@CompoundIndex(def = "{'countryName':1, 'name':1, 'telephone':1}", unique = true),
        @CompoundIndex(def = "{'telephone':1}", unique = true),
        @CompoundIndex(def = "{'countryName':1, 'name':1}", unique = false)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Builder
public class FormalCaregiver extends FormalCaregiverObject {

    @Id
    private String formalCaregiverId;

    @NotNull(message = "FormalCaregiver: El telefono del Cuidador Formal no puede ser nulo")
    @NotEmpty(message = "FormalCaregiver: El telefono del Cuidador Formal no puede ser vacío")
    @Size(min = 7, max = 20,
            message = "FormalCaregiver: El telefono del Cuidador Formal debe contener entre 7 y 20 caracteres")
    private String telephone;

    @NotNull(message = "FormalCaregiver: El mail del Cuidador Formal no puede ser nulo")
    @NotEmpty(message = "FormalCaregiver: El mail del Cuidador Formal no puede ser vacío")
    @Size(max = 50, message = "FormalCaregiver: El mail del Cuidador Formal no puede exceder los 50 caracteres")
    private String mail;

    @NotNull(message = "FormalCaregiver: La propiedad 'comments' del Cuidador Formal no puede ser nula")
    @Size(max = 100,
            message = "FormalCaregiver: Los comentarios del Cuidador Formal no pueden exceder los 100 caracteres")
    private String comments;

    /* Votos por cada puntaje.
       votes[0..4], donde: votes[n] es la cantidad de votos para el puntaje "n", con n=[0, 4].
            votes[0]: cantidad de votos para el puntaje 1
            ...
            votes[4]: cantidad de votos para el puntaje 5
     */
    //@NotNull(message = "FormalCaregiver: Clave 'votes[]' del Cuidador Formal no puede ser nulo")
    @Size(min = 5, max = 5, message = "FormalCaregiver: La cardinalidad de la propiedad 'votes[]' debe ser 5")
    @Builder.Default
    private int[] votes = new int[5];

    @Transient
    private double averageScore;

    @BooleanFlag
    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente

    @BooleanFlag
    private Boolean deleted;

    private byte[] photo;

    /* Zonas de interés del Cuidador Formal:
         -Si interestZones=[], implica que llega a todos los Departamentos/Provincias del pais.
         -Si interestZones[n] (es un departmentName registrado), con cities=[], implica que llega al departamento
            entero.
         -Si interestZones[n] tiene un departmentName con una cityName registrados, con neighborhoodNames=[], implica
            que llega a toda la ciudad/localidad de dicho departamento.
         -Si interestZones[n] tiene un departmentName, una cityName y barrios registrados, implica que llega solo a esos
         barrios de la ciudad.
    */
    @Builder.Default
    @Valid
    List<InterestZonesObject> interestZones = new ArrayList<>();

    @NotNull(message = "FormalCaregiver: El país del Cuidador Formal no puede ser nulo")
    @NotEmpty(message = "FormalCaregiver: El país del Cuidador Formal no puede ser vacío")
    @Size(max = 15,
            message = "FormalCaregiver: El pais del Cuidador Formal no puede exceder los 15 caracteres")
    private String countryName; // Pais de residencia del Cuidador Formal


    public double getAverageScore(){
        int votesCount = 0, votesByScore = 0;
        double votesSum = 0;
        for (int score = 1; score <= 5; score++) {
            votesByScore = votes[score-1];
            votesCount += votesByScore;
            votesSum += votesByScore * score;
        }
        return (votesCount > 0) ? (votesSum / votesCount) : 0;
    }
}
