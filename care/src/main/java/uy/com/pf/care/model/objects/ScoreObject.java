package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreObject {

    @NotNull(message = "ScoreObject: La fecha no puede ser nula")
    @PastOrPresent(message = "ScoreObject: La fecha debe ser anterior o igual a la fecha actual")
    private LocalDate date;

    @NotNull(message = "ScoreObject: El puntaje no puede ser nulo")
    @Range(min = 1, max = 5, message = "ScoreObject: El puntaje debe estar entre 1 y 5")
    private int score;

    @Size(max = 100, message = "ScoreObject: El comentario no puede exceder los 100 caracteres")
    private String comment;
}
