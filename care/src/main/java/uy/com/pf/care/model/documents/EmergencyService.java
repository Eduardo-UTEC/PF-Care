package uy.com.pf.care.model.documents;

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
    @CompoundIndex(
        def = "{'countryName':1, 'departmentName':1, 'cityName':1, 'name':1}",
        name = "country_department_city_name",
        unique = true
    )
})

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyService {
    @Id
    private String emergencyService_id;
    private String name;
    private Boolean deleted;

    private String cityName;
    private String departmentName;
    private String countryName;

}
