package uy.com.pf.care.model.objects;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TelephoneObject {
    private String countryCode;
    private String number;
}
