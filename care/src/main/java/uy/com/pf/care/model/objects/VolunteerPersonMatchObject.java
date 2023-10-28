package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerPersonMatchObject {
    private String volunteerPersonId;
    private boolean isMatch;
}
