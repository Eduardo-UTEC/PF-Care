package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.AddressObject;
import uy.com.pf.care.model.objects.TelephoneObject;

@Document("Residential")
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Residential {
    @Id
    private String residential_id;
    private String name;
    private TelephoneObject telephone;
    private AddressObject address;

    //private boolean used;       // Si está siendo utilizado, no se puede eliminar físicamente
    private Boolean deleted;    // Borrado lógico

    private String cityName;
    private String departmentName;
    private String countryName;

}
