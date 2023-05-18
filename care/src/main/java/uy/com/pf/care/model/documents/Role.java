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

import java.util.List;

@Document("Roles")

@CompoundIndexes({
    //@CompoundIndex(def = "{'userName':1}", unique = true)
})

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String roleId;

    @NotNull(message = "Role: El nombre del rol no puede ser nulo")
    @NotEmpty(message = "Role: El nombre del rol  no puede ser vacío")
    @Size(max = 20, message = "Role: El nombre del rol no puede exceder los 20 caracteres")
    private String name;

    @NotNull(message = "Role: La lista de tareas para el rol no puede ser nula")
    private List<String> tasks;

}
