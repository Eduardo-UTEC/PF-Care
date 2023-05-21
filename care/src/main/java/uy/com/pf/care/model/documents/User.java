package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.ZoneObject;

import java.util.List;

@Document("Users")

@CompoundIndexes({
    @CompoundIndex(def = "{'userName':1}", unique = true)
})

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    @NotNull(message = "User: El nombre de usuario no puede ser nulo")
    @NotEmpty(message = "User: El nombre de usuario  no puede ser vacío")
    @Size(max = 15, message = "User: El nombre de usuario no puede exceder los 15 caracteres")
    private Integer userName;

    @NotNull(message = "User: El password no puede ser nulo")
    @NotEmpty(message = "User: El password  no puede ser vacío")
    @Size(min = 7, message = "User: El password debe tener un mínimo de 7 caracteres")
    private String pass;

    @NotNull(message = "User: El usuario debe tener al menos un rol")
    @NotEmpty(message = "User: El usuario debe tener al menos un rol")
    private List<String> rolesId;

    @NotNull(message = "User: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

    @BooleanFlag
    private Boolean deleted;  // Si es true, debe ser true en el documento asociado a este usuario

}
