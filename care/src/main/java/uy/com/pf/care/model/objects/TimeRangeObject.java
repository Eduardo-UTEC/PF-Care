package uy.com.pf.care.model.objects;

import lombok.*;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TimeRangeObject {
    private LocalTime startTime;
    private LocalTime endTime;
}
