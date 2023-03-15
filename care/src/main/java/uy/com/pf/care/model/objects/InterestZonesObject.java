package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterestZonesObject {
    private String departmentName;
    private List<CityObject> cities = new ArrayList<>();

}
