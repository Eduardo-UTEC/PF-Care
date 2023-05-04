package uy.com.pf.care.model.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
//@ToString
public enum DaysWeekEnum {
    MON("Lunes"),
    TUE("Martes"),
    WED("Miércoles"),
    THU("Jueves"),
    FRI("Viernes"),
    SAT("Sábado"),
    SUN("Domingo");

    private String name;
}
