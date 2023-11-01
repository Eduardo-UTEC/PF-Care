package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import uy.com.pf.care.model.enums.DaysWeekEnum;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParamsDayTimeRangeAndExcludedIdsObject {

    @NotNull(message = "ParamsDayTimeRangeAndExcludedIdsObject: La propiedad 'dayTimeRange' no puede ser nula")
    @Valid
    private List<DayTimeRangeObject> dayTimeRange;

    private List<String> excludedVolunteerIds;
}
