package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @CompoundIndex(def = "{'identificationDocument':1, 'zone.countryName':1}", unique = true)
})

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    @NotNull(message = "User: El documento de identidad no puede ser nulo")
    private Integer identificationDocument;

    @NotNull(message = "User: El password no puede ser nulo")
    @NotEmpty(message = "User: El password  no puede ser vacío")
    @Size(min = 8, message = "User: El password debe tener un minimo de 8 caracteres")
    private String pass;

    @NotNull(message = "User: El usuario debe tener al menos un rol")
    private List<String> rolesId;

    @NotNull(message = "User: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

}
