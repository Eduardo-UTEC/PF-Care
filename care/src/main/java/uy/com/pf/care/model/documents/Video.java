package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Videos")

@CompoundIndexes({
    @CompoundIndex(
        def = "{'countryName':1, 'departmentName':1, 'url':1}",
        unique = true
    )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {

    @Id
    private String videoId;

    @Size(max = 100 , message = "VideoObject: La descripcion del video no puede exceder los 100 caracteres")
    private String description;

    @NotNull(message = "VideoObject: la url del video no puede ser nula")
    @NotEmpty(message = "VideoObject: la url del video no puede ser vacia")
    private String url;

    @Builder.Default
    @NotNull(message = "Role: La propiedad 'roles' no puede ser nula")
    private List<Integer> ordinalRoles = new ArrayList<>(); //Contiene el ordinal de RolesEnum

    @NotNull(message = "Video: El departamento no puede ser nulo")
    @NotEmpty(message = "Video: El departamento no puede ser vacio")
    @Size(max = 25 , message = "Video: El departamento no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "Video: El pais no puede ser nulo")
    @NotEmpty(message = "Video: El pais no puede ser vacio")
    @Size(max = 15 , message = "Video: El pais no puede exceder los 15 caracteres")
    private String countryName;
}
