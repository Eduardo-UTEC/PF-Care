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
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("VolunteerActivities")
@CompoundIndex(def = "{'countryName':1, 'departmentName':1, 'name':1}", unique = true)

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerActivity{

    @Id
    private String volunteerActivityId;

    @NotNull(message = "VolunteerActivity: El nombre de la actividad del voluntario no puede ser nulo")
    @NotEmpty(message = "VolunteerActivity: El nombre de la actividad del voluntario no puede ser vacio")
    @Size(max = 30 ,
            message = "VolunteerActivity: El nombre de la actividad del voluntario no puede exceder los 30 caracteres")
    private String name;

    @NotNull(message = "VolunteerActivity: La descripcion de la actividad del voluntario no puede ser nula")
    @Size(max = 100 , message =
            "VolunteerActivity: La descripcion de la actividad del voluntario no puede exceder los 100 caracteres")
    private String description;

    //Se toma al persistir.
    private LocalDate registrationDate;

    @BooleanFlag
    private Boolean deleted;

    @NotNull(message = "VolunteerActivity: El departamento no puede ser nulo")
    @NotEmpty(message = "VolunteerActivity: El departamento no puede ser vacio")
    @Size(max = 25 , message = "VolunteerActivity: El departamento no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "VolunteerActivity: El pais no puede ser nulo")
    @NotEmpty(message = "VolunteerActivity: El pais no puede ser vacio")
    @Size(max = 15 , message = "VolunteerActivity: El pais no puede exceder los 15 caracteres")
    private String countryName;

}
