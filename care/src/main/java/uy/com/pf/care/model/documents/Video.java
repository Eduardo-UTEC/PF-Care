package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Videos")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    @Id
    private String videoId;

    @Size(max = 100 , message = "VideoObject: La descripcion del video no puede exceder los 100 caracteres")
    private String description;

    @NotNull(message = "VideoObject: la url del video no puede ser nula")
    @NotEmpty(message = "VideoObject: la url del video no puede ser vacia")
    private String url;
}
