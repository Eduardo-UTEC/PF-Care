package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HealthProviders")
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthProvider {
    @Id
    private String healthProvider_id;
    private String name;
    //private boolean used;       // Si está siendo utilizado, no se puede eliminar físicamente
    private Boolean deleted;

    private String cityName;
    private String departmentName;
    private String countryName;

}
