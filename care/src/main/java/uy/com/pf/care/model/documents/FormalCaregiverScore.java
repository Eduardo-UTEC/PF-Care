package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.ScoreObject;

import java.time.LocalDate;

@Document("FormalCaregiversScores")
@CompoundIndexes({
        @CompoundIndex(def = "{'formalCaregiverId': 1, 'patientId': 1}", unique = true),
        @CompoundIndex(def = "{'formalCaregiverId': 1, 'date': -1}", unique = false)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FormalCaregiverScore extends ScoreObject {
    @Id
    private String formalCaregiverScoreId;
    private String formalCaregiverId;
    private String patientId;
}
