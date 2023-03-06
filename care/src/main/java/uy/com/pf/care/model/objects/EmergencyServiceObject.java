package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmergencyServiceObject {
    private String name;
    // Se guarda el Id para evitar duplicar datos de city+department+country
    private String emergencyServiceId;
}
