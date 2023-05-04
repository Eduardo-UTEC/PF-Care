package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.Null;
import lombok.*;

import java.time.LocalTime;

@Data
public class TimeRangeObject {
    private LocalTime startTime;
    private LocalTime endTime;
}
