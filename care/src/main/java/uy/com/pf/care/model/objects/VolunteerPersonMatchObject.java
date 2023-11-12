package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerPersonMatchObject {
    private String volunteerPersonId;
    private boolean isMatch;
    private LocalDate shippingDate;
    private LocalDate confirmationDate;
}
