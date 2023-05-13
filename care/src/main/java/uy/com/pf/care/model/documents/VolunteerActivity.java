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

@Document("VolunteerActivities")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActivity {

    @Id
    private String volunteerActivityId;

    @NotNull(message = "VolunteerActivity: El nombre de la actividad del voluntario no puede ser nulo")
    @NotEmpty(message = "VolunteerActivity: El nombre de la actividad del voluntario no puede ser vacio")
    @Size(max = 15 ,
            message = "VolunteerActivity: El nombre de la actividad del voluntario no puede exceder los 15 caracteres")
    private String name;

    @NotNull(message = "VolunteerActivity: La propiedad 'deleted' de la actividad del voluntario no puede ser nula")
    @BooleanFlag
    private Boolean deleted;
}
