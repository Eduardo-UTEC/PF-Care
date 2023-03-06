package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressObject {
    private String street;
    private Integer portNumber;
    private String betweenStreet1;
    private String betweenStreet2;
    private Long lat, lon;
}
