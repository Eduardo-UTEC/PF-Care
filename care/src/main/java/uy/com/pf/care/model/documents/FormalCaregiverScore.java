package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.FeelingObject;

import java.time.LocalDate;

@Document("FormalCaregiversScores")
@CompoundIndexes({
        @CompoundIndex(def = "{'formalCaregiverId': 1, 'patientId': 1}", unique = true),
        @CompoundIndex(def = "{'formalCaregiverId': 1, 'date': -1}")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FormalCaregiverScore {

    @Id
    private String formalCaregiverScoreId;

    @NotNull(message = "FormalCaregiverScore: formalCaregiverId no puede ser nulo")
    @NotEmpty(message = "FormalCaregiverScore: formalCaregiverId no puede ser vacío")
    private String formalCaregiverId;

    @NotNull(message = "FormalCaregiverScore: patientId no puede ser nulo")
    @NotEmpty(message = "FormalCaregiverScore: patientId no puede ser vacío")
    private String patientId;

    @NotNull(message = "ScoreObject: La fecha no puede ser nula")
    @PastOrPresent(message = "ScoreObject: La fecha debe ser anterior o igual a la fecha actual")
    private LocalDate date;

    @NotNull(message = "ScoreObject: El puntaje no puede ser nulo")
    @Range(min = 1, max = 5, message = "ScoreObject: El puntaje debe estar entre 1 y 5")
    private Integer score;

    @Size(max = 100, message = "ScoreObject: El comentario no puede exceder los 100 caracteres")
    private String comment;

    private FeelingObject feeling;
}
