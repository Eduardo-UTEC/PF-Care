package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.ZoneObject;

@Document("Zones")
@CompoundIndexes({
        @CompoundIndex(
                def = "{'countryName':1, " +
                        "'departmentName':1, " +
                        "'cityName':1, " +
                        "'neighborhoodName':1}",
                name = "country_department_city_neighborhood",
                unique = true
        )
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Zone extends ZoneObject {
    @Id
    private String zone_id;
    private Boolean deleted;
}
