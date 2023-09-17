package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.PersonGenderEnum;
import uy.com.pf.care.model.enums.RoleEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleObject {

    @NotNull(message = "RoleObject: La clave 'roleId' no puede ser nula")
    @NotEmpty(message = "RoleObject: La clave 'roleId' no puede ser vacía")
    private String roleId;

    @NotNull(message = "RoleObject: La clave 'rolName' no puede ser nula")
    private RoleEnum rol;
}
