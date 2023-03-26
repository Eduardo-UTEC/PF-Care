package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.FormalCaregiverObject;
import uy.com.pf.care.model.objects.InterestZonesObject;

import java.util.ArrayList;
import java.util.List;

@Document("FormalCaregivers")
@CompoundIndexes({
    @CompoundIndex(
            def = "{'mail':1}",
            name = "mail",
            unique = true
    ),
    @CompoundIndex(
            def = "{'countryName':1, 'deleted':1}",
            name = "country_deleted",
            unique = false
    ),
    @CompoundIndex(
            def = "{'name':1, 'countryName':1, 'deleted':1, 'telephone':1}",
            name = "name_country_deleted_telephone",
            unique = true
    )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FormalCaregiver extends FormalCaregiverObject {
    @Id
    private String formalCaregiver_id;
    private String telephone;
    private String mail;
    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente
    private String comments;
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

}
