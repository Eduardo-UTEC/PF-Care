package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserObject {
    //Si es un paciente, entityId es el Id del paciente. Si es un cuidador formal, es el Id del cuidador formal...
    private String entityId;
    private RoleObject role;
}
