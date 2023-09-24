package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("EmergencyServices")

@CompoundIndexes({
    @CompoundIndex(def = "{'countryName':1, 'departmentName':1, 'cityName':1, 'name':1}", unique = true)
})

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyService {

    @Id
    private String emergencyServiceId;

    @NotNull(message = "EmergencyService: El nombre del Servicio de Emergencia no puede ser nulo")
    @NotEmpty(message = "EmergencyService: El nombre del Servicio de Emergencia no puede ser vacío")
    @Size(max = 30,
            message = "EmergencyService: El nombre del Servicio de Emergencia no puede exceder los 30 caracteres")
    private String name;

    @BooleanFlag
    private Boolean deleted;

    @NotNull(message = "EmergencyService: La ciudad del Servicio de Emergencia no puede ser nula")
    @NotEmpty(message = "EmergencyService: La ciudad del Servicio de Emergencia no puede ser vacía")
    @Size(max = 25,
            message = "EmergencyService: La ciudad del Servicio de Emergencia no puede exceder los 25 caracteres")
    private String cityName;

    @NotNull(message = "EmergencyService: El departamento del Servicio de Emergencia no puede ser nulo")
    @NotEmpty(message = "EmergencyService: El departamento del Servicio de Emergencia no puede ser vacío")
    @Size(max = 25,
            message = "EmergencyService: El departamento del Servicio de Emergencia no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "EmergencyService: El país del Servicio de Emergencia no puede ser nulo")
    @NotEmpty(message = "EmergencyService: El país del Servicio de Emergencia no puede ser vacío")
    @Size(max = 15,
            message = "EmergencyService: El pais  del Servicio de Emergencia no puede exceder los 15 caracteres")
    private String countryName;
}
