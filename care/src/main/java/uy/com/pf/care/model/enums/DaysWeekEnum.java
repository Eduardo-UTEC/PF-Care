package uy.com.pf.care.model.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@Getter
public enum DaysWeekEnum {
    MON(0, "Lunes"),
    TUE(1, "Martes"),
    WED(2, "Miércoles"),
    THU(3, "Jueves"),
    FRI(4, "Viernes"),
    SAT(5, "Sábado"),
    SUN(6, "Domingo");

    private final int ordinal;
    private final String name;
}
