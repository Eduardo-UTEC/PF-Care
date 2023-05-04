package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import uy.com.pf.care.model.enums.FormalCaregiversEnum;

@Data
@EqualsAndHashCode(callSuper=false)
public class FormalCaregiverObject extends CarerObject{

    @NotNull(message = "FormalCaregiverObject: La propiedad 'type' no puede ser nula")
    private FormalCaregiversEnum type;

    @NotNull(message = "FormalCaregiverObject: La propiedad 'priceHour' no puede ser nula")
    @PositiveOrZero(message = "FormalCaregiverObject: La propiedad 'priceHour' no puede ser < 0")
    private Integer priceHour;

}
