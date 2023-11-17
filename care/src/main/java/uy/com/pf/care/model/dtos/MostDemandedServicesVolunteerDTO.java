package uy.com.pf.care.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostDemandedServicesVolunteerDTO {
    //private String volunteerServiceId;
    private String volunteerServiceName;
    private int offerCount;
    private int offerPercentage;
}
