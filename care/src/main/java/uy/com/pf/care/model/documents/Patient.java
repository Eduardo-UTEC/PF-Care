package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.*;

import java.util.ArrayList;
import java.util.List;

@Document("Patients")
@CompoundIndexes({
        @CompoundIndex(def = "{'identificationDocument':1, 'zone.countryName':1}", unique = true),
        @CompoundIndex(def = "{'mail':1}", unique = true),
        @CompoundIndex(def = "{'zone.countryName':1, 'zone.departmentName':1, 'zone.cityName':1, 'zone.neighborhoodName':1, " +
                        "'name1':1}")
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends PersonObject {

    @Id
    private String patientId;

    //@NotNull(message = "Patient: El Paciente debe estar vinculado a un Usuario (clave 'userId' no puede ser nula)")
    //@NotEmpty(message = "Patient: El Paciente debe estar vinculado a un Usuario (clave 'userId' no puede ser vacia)")
    private String userId;

    private String referenceCaregiverId;

        //@Valid
    private List<String> formalCaregiversId = new ArrayList<>();

    //@Valid
    private List<InformalCaregiverObject> informalCaregivers = new ArrayList<>();

    @NotNull(message = "Patient: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

    //@NotNull(message = "Patient: La clave 'healthProvider' no puede ser nula")
    //private HealthProviderObject healthProvider;
    private String healthProviderId;

    //@NotNull(message = "Patient: La clave 'emergencyService' no puede ser nula")
    //private EmergencyServiceObject emergencyService;
    private String emergencyServiceId;

    //@NotNull(message = "Patient: La clave 'residential' no puede ser nula")
    //private ResidentialObject residential;
    private String residentialId;

    @BooleanFlag
    private Boolean validate;

    @BooleanFlag
    private Boolean deleted;
}
