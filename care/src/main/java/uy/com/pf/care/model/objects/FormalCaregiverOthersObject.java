package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormalCaregiverOthersObject extends FormalCaregiverObject{

    @NotNull(message = "FormalCaregiverOthersObject: La clave 'formalCaregiverId' no puede ser nula")
    private String formalCaregiverId;

}
