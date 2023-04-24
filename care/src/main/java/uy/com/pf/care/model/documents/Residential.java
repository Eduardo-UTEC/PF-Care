package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.AddressObject;

@Document("Residential")
@CompoundIndexes({
        @CompoundIndex(
                def = "{'countryName':1, 'departmentName':1, 'cityName':1, 'name':1, 'address.street':1, " +
                        "'address.portNumber':1}",
                unique = true
        )
})
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Residential {
    @Id
    private String residentialId;
    private String name;
    private String telephone;
    private AddressObject address;
    private Boolean deleted;

    private String cityName;
    private String departmentName;
    private String countryName;

}
