package uy.com.pf.care.model.documents;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
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
import uy.com.pf.care.model.enums.RoleEnum;

import java.util.List;
/*
@CompoundIndexes({
    @CompoundIndex(
        def = "{'countryName':1, 'departmentName':1, 'roleName':1}",
        unique = true
    )
})
@Document("Roles")

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String roleId;

    @NotNull(message = "Role: El nombre del rol no puede ser nulo")
    private RoleEnum roleName;

    @NotNull(message = "Role: El departamento no puede ser nulo")
    @NotEmpty(message = "Role: El departamento no puede ser vacio")
    @Size(max = 25 , message = "Role: El departamento no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "Role: El pais no puede ser nulo")
    @NotEmpty(message = "Role: El pais no puede ser vacio")
    @Size(max = 15 , message = "Role: El pais no puede exceder los 15 caracteres")
    private String countryName;

}


 */