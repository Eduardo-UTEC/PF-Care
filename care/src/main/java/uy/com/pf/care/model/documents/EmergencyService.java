package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("EmergencyServices")
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyService {
    @Id
    private String emergencyService_id;
    private String name;
    //private boolean used;       // Si está siendo utilizado, no se puede eliminar físicamente
    private boolean deleted;

    private String cityName;
    private String departmentName;
    private String countryName;

}
