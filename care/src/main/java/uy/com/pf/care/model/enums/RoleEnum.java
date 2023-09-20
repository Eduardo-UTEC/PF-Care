package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    WEB_ADMIN(0, "Administrador Web"),
    PATIENT(1, "Paciente"),
    REFERRING_CARE(2, "Cuidador Referente"),
    FORMAL_CARE(3, "Cuidador Formal"),
    VOLUNTEER_PERSON(4, "Persona Voluntaria"),
    VOLUNTEER_COMPANY(5, "Compañía Voluntaria");

    private final int ordinal;
    private final String name;

}
