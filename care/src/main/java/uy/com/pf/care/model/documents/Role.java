package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.enums.RoleEnum;
import uy.com.pf.care.model.objects.VideoObject;
import uy.com.pf.care.model.objects.ZoneObject;

import java.util.List;

@Document("Roles")

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String roleId;

    @NotNull(message = "Role: El nombre del rol no puede ser nulo")
    @NotEmpty(message = "Role: El nombre del rol  no puede ser vacío")
    private RoleEnum roleName;

    //@NotNull(message = "Role: La lista de tareas para el rol no puede ser nula")
    //private List<String> tasks;

    @NotNull(message = "Role: La propiedad 'videos' no puede ser nula")
    private List<VideoObject> videos;

    @NotNull(message = "Role: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

}
