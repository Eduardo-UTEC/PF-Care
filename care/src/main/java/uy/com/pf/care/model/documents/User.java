package uy.com.pf.care.model.documents;

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
import uy.com.pf.care.model.objects.LoginObject;
import uy.com.pf.care.model.objects.RoleObject;
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
public class User extends LoginObject {

    @Id
    private String userId;

    @NotNull(message = "User: El usuario debe tener al menos un rol")
    @NotEmpty(message = "User: El usuario debe tener al menos un rol")
    private List<RoleObject> roles;

    @NotNull(message = "User: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

}
