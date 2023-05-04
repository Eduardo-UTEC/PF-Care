package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class CarerObject {

    @NotNull(message = "CarerObject: La propiedad 'name' no puede ser nula")
    @NotEmpty(message = "CarerObject: La propiedad 'name' no puede ser vacía")
    private String name;    // Puede ser Formal o Informal

    // Dias y rangos horarios semanales disponibles para el cuidado.
    // Si el cuidador tiene disponibilidad 24x7, dayTimeRange=[]
    @Valid
    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();

}
