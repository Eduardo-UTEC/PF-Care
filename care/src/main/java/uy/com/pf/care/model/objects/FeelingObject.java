package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FeelingObject {

    // Segun el tipo de retorno de la libreria  Stanford CoreNLP, podría ser: 1: positivo, 0: neutro, -1: negativo
    private int feelingState;

}
