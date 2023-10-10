package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uy.com.pf.care.model.enums.RelationshipEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientLinkedReferentObject_borrar {
    
    private String patientId;
    
    @NotNull(message = "La clave relatioship no puede ser nula")
    private RelationshipEnum relationship; 
}
