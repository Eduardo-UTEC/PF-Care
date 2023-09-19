package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.RoleEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoObject {
    private String videoId;
    private String description;
    private String url;
}
