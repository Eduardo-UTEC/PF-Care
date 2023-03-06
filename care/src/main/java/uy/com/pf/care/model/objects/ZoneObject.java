package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneObject {
    private String neighborhoodName;
    private String cityName;
    private String departmentName;
    private String countryName;
}
