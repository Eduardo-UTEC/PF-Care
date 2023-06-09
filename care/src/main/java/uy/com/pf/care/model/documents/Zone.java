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
import org.springframework.validation.annotation.Validated;
import uy.com.pf.care.model.objects.ZoneObject;

@Document("Zones")
@CompoundIndexes({
        @CompoundIndex(
                def = "{'countryName':1, 'departmentName':1, 'cityName':1, 'neighborhoodName':1}",
                unique = true
        )
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Zone extends ZoneObject {

    @Id
    private String zoneId;

    @BooleanFlag
    private Boolean deleted;
}
