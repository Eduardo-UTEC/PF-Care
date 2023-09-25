package uy.com.pf.care.model.documents;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Materials")
//@CompoundIndex(def = "{'countryName':1, 'departmentName':1, 'name':1}", unique = true)
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Material {

    @Id
    private String materialId;

    @NotNull(message = "La clave 'name' no puede ser nula")
    @NotEmpty(message = "La clave 'name' no puede ser vacía")
    @Size(max = 80, message = "La clave 'name' no puede exceder los 80 caracteres")
    private String name;

    @Size(max = 200, message = "La clave 'name' no puede exceder los 200 caracteres")
    private String description;

    private Byte[] photo;

    @NotNull(message = "El país de registro del material no puede ser nulo")
    @NotEmpty(message = "El país de registro del material no puede ser vacío")
    @Size(max = 30, message = "El país de registro del material no puede exceder los 30 caracteres")
    private String countryName;

    @BooleanFlag
    private Boolean deleted;

}
