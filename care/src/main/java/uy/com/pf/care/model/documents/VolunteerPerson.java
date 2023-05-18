package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.DayTimeRangeObject;
import uy.com.pf.care.model.objects.PersonObject;

import java.util.ArrayList;
import java.util.List;

@Document("VolunteersPersons")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class VolunteerPerson extends PersonObject {

    //TODO: zonas preferidas

    @Valid
    private List<DayTimeRangeObject> dayTimeRanges = new ArrayList<>(); // Rangos de dias y horarios disponibles

    private byte[] photo;

}
