package uy.com.pf.care.model.objects;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NeighborhoodObject {
    @Id
    private String zoneId;
    private String neighborhoodName;
}
