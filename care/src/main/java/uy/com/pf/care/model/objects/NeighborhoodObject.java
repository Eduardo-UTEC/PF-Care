package uy.com.pf.care.model.objects;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class NeighborhoodObject {
    private String zoneId;
    private String neighborhoodName;
}
