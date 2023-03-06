package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CarerObject {
    private String name;    // Puede ser Formal o Informal

    // Dias y rangos horarios semanales disponibles para el cuidado.
    // Si el cuidador tiene disponibilidad 24x7, dayTimeRange=[]
    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();

}
