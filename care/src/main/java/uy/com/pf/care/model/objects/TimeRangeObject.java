package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeRangeObject {

    @NotNull(message = "TimeRangeObject: La propiedad startTime no puede ser nula")
    private LocalTime startTime;

    @NotNull(message = "TimeRangeObject: La propiedad endTime no puede ser nula")
    private LocalTime endTime;
}
