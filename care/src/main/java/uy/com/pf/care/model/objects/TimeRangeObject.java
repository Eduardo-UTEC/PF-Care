package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalTime;

@Data
public class TimeRangeObject {

    @NotNull(message = "TimeRangeObject: La propiedad startTime no puede ser nula")
    private LocalTime startTime;

    @NotNull(message = "TimeRangeObject: La propiedad endTime no puede ser nula")
    private LocalTime endTime;
}
