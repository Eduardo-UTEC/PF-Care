package uy.com.pf.care.model.objects;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityObject {
    private String cityName;
    private List<String> neighborhoodNames;
}
