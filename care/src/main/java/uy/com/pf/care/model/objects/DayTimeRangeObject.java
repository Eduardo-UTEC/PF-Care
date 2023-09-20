package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uy.com.pf.care.model.enums.DaysWeekEnum;

import java.util.ArrayList;
import java.util.List;

@Data
public class DayTimeRangeObject {

    @NotNull(message = "DayTimeRangeObject: La propiedad 'day' no puede ser nula")
    private DaysWeekEnum day;

    @Valid
    private List<TimeRangeObject> timeRange = new ArrayList<>();
}
