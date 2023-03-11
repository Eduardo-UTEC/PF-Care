package uy.com.pf.care.model.objects;

import lombok.*;
import uy.com.pf.care.model.enums.RelationshipEnum;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InformalCaregiverObject extends CarerObject{
    private String telephone;
    private RelationshipEnum relationship;
}
