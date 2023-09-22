package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uy.com.pf.care.model.enums.RoleEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserObject {
    //Si es un paciente, entityId es el Id del paciente. Si es un cuidador formal, es el Id del cuidador formal...
    private String entityId;
    //private RoleObject role;
    @NotNull(message = "RoleObject: La clave 'rolName' no puede ser nula")
    private RoleEnum rol;
}
