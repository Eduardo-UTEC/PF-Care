package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.FormalCaregiversEnum;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class FormalCaregiverObject extends CarerObject{
    private FormalCaregiversEnum type;  //Ej: servicio de acompañantes    / enfermero
    private Integer priceHour;          // Precio por hora en moneda nacional


}
