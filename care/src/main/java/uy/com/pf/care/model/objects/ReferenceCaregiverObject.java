package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.RelationshipEnum;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceCaregiverObject extends PersonObject {
    private RelationshipEnum relationship;
    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();  //Dias y rangos horarios semanales para el cuidado
}
