package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
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
