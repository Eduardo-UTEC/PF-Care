package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.RoleEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDonationRequestObject {
    private String userId;
    private RoleEnum rol;
}
