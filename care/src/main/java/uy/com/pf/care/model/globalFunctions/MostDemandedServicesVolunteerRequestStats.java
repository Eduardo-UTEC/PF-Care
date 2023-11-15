package uy.com.pf.care.model.globalFunctions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.round;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostDemandedServicesVolunteerRequestStats {
    private String volunteerServiceName;
    private int demandCount = 0;
    private int demandPercentage = 0; //Se carga a nivel superior

    public void incrementDemandCount() {
        demandCount++;
    }

}
