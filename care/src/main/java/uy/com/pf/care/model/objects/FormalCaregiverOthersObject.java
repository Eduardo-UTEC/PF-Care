package uy.com.pf.care.model.objects;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormalCaregiverOthersObject extends FormalCaregiverObject{
    private String formalCaregiverId;
}
