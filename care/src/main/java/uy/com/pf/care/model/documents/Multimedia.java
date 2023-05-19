package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.VideoObject;

import java.util.List;

@Document("Multimedia")

@CompoundIndexes({
    //@CompoundIndex(def = "{'userName':1}", unique = true)
})

//@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Multimedia {

    @Id
    private String multimediaId;

    @NotNull(message = "Multimedia: La propiedad 'roleId' no puede ser nula")
    @NotEmpty(message = "Multimedia: La propiedad 'roleId' no puede ser vacia")
    private String roleId;

    @NotNull(message = "Multimedia: La propiedad 'videos' no puede ser nula")
    private List<VideoObject> videos;

}
