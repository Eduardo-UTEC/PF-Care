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
import uy.com.pf.care.model.objects.TelephoneObject;

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
            def = "{'name':1, 'telephone':1, 'departmentName':1, 'countryName':1}",
            name = "name_telephone_department_country",
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
    private TelephoneObject telephone;
    private String mail;
    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente
    private String comments;

    // Ciudades/Localidades o Barrios de interés del Cuidador Formal.
    // Si interestZones=[], implica que llega a todas las zonas del Departamento/Provincia
    List<String> interestZones = new ArrayList<>();

    private String departmentName;
    private String countryName;

    //private boolean used;       // Si está siendo utilizado, no se puede eliminar físicamente
    private Boolean deleted;

}
