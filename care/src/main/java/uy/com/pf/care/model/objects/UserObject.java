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
    //Si es un paciente, objectId es el Id del paciente. Si es un cuidador formal, es el Id del cuidador formal...
    private String objectId;
    private RoleObject role;
}
