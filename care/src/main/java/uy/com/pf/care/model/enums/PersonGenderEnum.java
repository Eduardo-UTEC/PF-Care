package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum PersonGenderEnum {
    MALE("Masculino"),
    FEMALE("Femenino"),
    TRANS("Trans"),
    TRANSM("Trans Masculino"),
    TRANSF("Trans Femenino");

    private String name;
}
