package uy.com.pf.care.model.documents;

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
        @CompoundIndex(
            def = "{'identificationDocument':1, 'zone.countryName':1}",
            name = "identificationDocument_country",
            unique = true
        ),
        @CompoundIndex(
                def = "{'mail':1}",
                name = "mail",
                unique = true
        ),
        @CompoundIndex(
                def = "{'zone.countryName':1, " +
                        "'zone.departmentName':1, " +
                        "'zone.cityName':1, " +
                        "'zone.neighborhoodName':1, " +
                        "'name1':1}",
                name = "country_department_city_neighborhood_name1",
                unique = false
        )
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Patient extends PersonObject {
    @Id
    private String patient_id;
    private ZoneObject zone;
    private ReferenceCaregiverObject referenceCaregiver;
    private List<FormalCaregiverOthersObject> formalCaregivers = new ArrayList<>();
    private List<InformalCaregiverObject> informalCaregivers = new ArrayList<>();
    private HealthProviderObject healthProvider;
    private EmergencyServiceObject emergencyService;
    private ResidentialObject residential;
    private Boolean deleted;
}
