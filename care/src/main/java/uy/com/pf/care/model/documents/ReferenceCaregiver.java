package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.PatientLinkedReferentObject;
import uy.com.pf.care.model.objects.PersonObject;
import uy.com.pf.care.model.objects.ZoneObject;

import java.util.ArrayList;
import java.util.List;

@Document("ReferenceCaregivers")
@CompoundIndexes({
        @CompoundIndex(def = "{'identificationDocument':1, 'zone.countryName':1}", unique = true),
        @CompoundIndex(def =
                "{'zone.countryName':1, " +
                "'zone.departmentName':1, " +
                "'zone.cityName':1, " +
                "'zone.neighborhoodName':1, " +
                "'name1':1}")
})
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceCaregiver extends PersonObject {
    @Id
    private String referenceCaregiverId;

    //@NotNull(message = "ReferenceCaregiver: El Cuidador Referente debe estar vinculado a un Usuario (clave 'userId' no puede ser nula)")
    //@NotEmpty(message = "ReferenceCaregiver: El Cuidador Referente debe estar vinculado a un Usuario (clave 'userId' no puede ser vacia)")
    private String userId;

    //@NotNull(message = "ReferenceCaregiver: El Cuidador Referente debe estar vinculado al menos a un Paciente  (clave 'patientsId' no puede ser nula)")
    //@NotEmpty(message = "ReferenceCaregiver: El Cuidador Referente debe estar vinculado al menos a un Paciente (clave 'patientsId' no puede ser vacia)")
    @Valid
    private List<PatientLinkedReferentObject> patients;

    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();  //Dias y rangos horarios semanales para el cuidado

    @NotNull(message = "ReferenceCaregiver: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

}
