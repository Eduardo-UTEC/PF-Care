package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoObject {

    @Size(max = 100 , message = "VideoObject: La descripcion del video no puede exceder los 100 caracteres")
    private String description;

    @NotNull(message = "VideoObject: la url del video no puede ser nula")
    @NotEmpty(message = "VideoObject: la url del video no puede ser vacia")
    private String url;
}
