package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PersonGenderEnum {
    MALE(0, "Masculino"),
    FEMALE(1, "Femenino"),
    TRANS(2, "Trans"),
    TRANSM(3, "Trans Masculino"),
    TRANSF(4, "Trans Femenino");

    private final int ordinal;
    private final String name;
}
