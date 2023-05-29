package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.enums.ContactMethodsEnum;
import uy.com.pf.care.model.objects.*;

import java.util.ArrayList;
import java.util.List;

@Document("VolunteersPersons")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerPerson extends PersonObject {

    @Builder.Default
    @Valid
    List<InterestZonesObject> interestZones = new ArrayList<>();

    @Builder.Default
    @Valid
    private List<DayTimeRangeObject> dayTimeRanges = new ArrayList<>(); // Rangos de dias y horarios disponibles

    @Builder.Default
    @Valid
    List<VolunteerActivityObject> activities = new ArrayList<>();

    private byte[] photo;

    @NotNull(message = "VolunteerPerson: La clave 'zone' no puede ser nula")
    private ZoneObject zone;

    @NotNull(message = "VolunteerPerson: La clave 'training' no puede ser nula")
    @Size(max = 50, message = "VolunteerPerson: la formación no debe exceder los 50 caracteres")
    private String training;

    @NotNull(message = "VolunteerPerson: La clave 'experience' no puede ser nula")
    @Size(max = 200, message = "VolunteerPerson: la experiencia no debe exceder los 200 caracteres")
    private String experience;

    @NotNull(message = "VolunteerPerson: La clave 'reasonToVolunteer' no puede ser nula")
    @Size(max = 100, message = "VolunteerPerson: la razon de ser voluntario no debe exceder los 100 caracteres")
    private String reasonToVolunteer;

    private List<ContactMethodsEnum> contactMethods;

    @BooleanFlag
    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente

    @NotNull(message = "VolunteerPerson: El país de la persona voluntaria no puede ser nulo")
    @NotEmpty(message = "VolunteerPerson: El país de la persona voluntaria no puede ser vacío")
    @Size(max = 15,
            message = "VolunteerPerson: El pais de la persona voluntaria no puede exceder los 15 caracteres")
    private String countryName; // Pais de residencia de la persona voluntaria

    @BooleanFlag
    private Boolean deleted;

}
