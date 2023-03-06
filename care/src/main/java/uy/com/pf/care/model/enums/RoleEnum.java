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
    ADMIN("Administrador"),
    PATIENT("Paciente"),
    CARE("Cuidador"),
    VOLUNTEER("Voluntario"),
    COMPANY("Empresa");

    private String name;
}
