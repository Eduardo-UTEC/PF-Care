package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.LoginObjectRegister;
import uy.com.pf.care.model.objects.UserObject;
import uy.com.pf.care.model.objects.ZoneObject;

import java.util.List;

@Document("Users")
@CompoundIndexes({
    @CompoundIndex(def = "{'userName':1}", unique = true)
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class User extends LoginObjectRegister {

    @Id
    private String userId;

    @NotNull(message = "El nombre del Usuario no puede ser nulo")
    @NotEmpty(message = "El nombre del Usuario no puede ser vacío")
    @Size(max = 30, message = "El nombre del Usuario no puede exceder los 30 caracteres")
    private String name;

    @NotNull(message = "El apellido del Usuario no puede ser nulo")
    @NotEmpty(message = "El nombre del Usuario no puede ser vacío")
    @Size(max = 30, message = "El nombre del Usuario no puede exceder los 30 caracteres")
    private String surname; // Si es empresa, queda en null

    //@NotNull(message = "User: El usuario debe tener al menos un rol")
    //@NotEmpty(message = "User: El usuario debe tener al menos un rol")
    @Valid
    private List<UserObject> roles;

    //Zona donde vive el usuario (si es empresa, es la zona donde está ubicada)
    @NotNull(message = "User: La clave 'residenceZone' no puede ser nula")
    private ZoneObject residenceZone;

}
