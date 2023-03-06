package uy.com.pf.care.model.objects;

import lombok.*;
import uy.com.pf.care.model.enums.DaysWeekEnum;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DayTimeRangeObject {
    private DaysWeekEnum day;
    @Builder.Default
    private List<TimeRangeObject> timeRange = new ArrayList<>();
}
