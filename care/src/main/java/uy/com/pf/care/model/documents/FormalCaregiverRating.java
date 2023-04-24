package uy.com.pf.care.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("FormalCaregiversRatings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormalCaregiverRating {
    @Id
    private String formalCaregiverRatingId;
    @Indexed(unique = false)
    private String formalCaregiverId;
    private Integer score;
    private String comment;
}
