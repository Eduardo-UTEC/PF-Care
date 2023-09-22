package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    PATIENT(0, "Paciente"),
    REFERENCE_CARE(1, "Cuidador Referente"),
    FORMAL_CARE(2, "Cuidador Formal"),
    VOLUNTEER_PERSON(3, "Persona Voluntaria"),
    VOLUNTEER_COMPANY(4, "Compañía Voluntaria");

    private final int ordinal;
    private final String name;

}
