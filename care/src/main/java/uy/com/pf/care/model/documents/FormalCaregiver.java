package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.FormalCaregiverObject;
import uy.com.pf.care.model.objects.TelephoneObject;

import java.util.ArrayList;
import java.util.List;

@Document("FormalCaregivers")
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
    private String comments;    // Comentarios que desee agregar el Cuidador Formal

    // Ciudades/Localidades o Barrios de interés del Cuidador Formal.
    // Si interestZones=[], implica que llega a todas las zonas del Departamento/Provincia
    List<String> interestZones = new ArrayList<>();

    private String departmentName;
    private String countryName;

    //private boolean used;       // Si está siendo utilizado, no se puede eliminar físicamente
    private boolean deleted;    // Borrado lógico

}
