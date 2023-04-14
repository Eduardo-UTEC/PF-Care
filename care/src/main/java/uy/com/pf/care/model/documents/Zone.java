package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.ZoneObject;

import java.util.ArrayList;
import java.util.List;

@Document("Zones")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class Zone extends ZoneObject {
    @Id
    private String zone_id;
    private List<String> patientsId = new ArrayList<>();         // Pacientes registrados en esta zona
    private List<String> volunteerPersonsId = new ArrayList<>(); // Personas Voluntarias registradas en esta zona
    // Borrado lógico.
    // Nota: un borrado fisico puede ejecutarse si patientsId y volunteerPersonsId están vacias.
    private Boolean deleted;
}
