package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum RoleEnum {
    WEB_ADMIN("Administrador Web"),
    PATIENT("Paciente"),
    REFERRING_CARE("Cuidador Referente"),
    FORMAL_CARE("Cuidador Formal"),
    VOLUNTEER_PERSON("Persona Voluntaria"),
    VOLUNTEER_COMPANY("Compañía Voluntaria");

    private String name;
}
