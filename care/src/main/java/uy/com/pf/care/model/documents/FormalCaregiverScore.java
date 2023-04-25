package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.VoteObject;

@Document("FormalCaregiversScores")
@CompoundIndexes({
        @CompoundIndex(def = "{'formalCaregiverId': 1, 'patientId': 1}", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormalCaregiverScore extends VoteObject {
    @Id
    private String formalCaregiverRatingId;
    private String formalCaregiverId;
    private String patientId;
}
