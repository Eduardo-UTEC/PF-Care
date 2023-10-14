package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginObjectRegister {

    /*@NotNull(message = "User: La clave 'userName' no puede ser nula")
    @NotEmpty(message = "User: La clave 'userName' no puede ser vacia")
    @Size(min = 5, max = 30, message = "User: La clave 'userName' debe contener entre 5 y 30 caracteres")
    private String userName;
     */
    @NotNull(message = "La clave 'identificationDocument' no puede ser nula")
    @Positive(message = "La clave 'identificationDocument' debe contener dígitos")
    private Integer identificationDocument;

    @NotNull(message = "User: El password no puede ser nulo")
    @NotEmpty(message = "User: El password  no puede ser vacío")
    @Size(min = 8, message = "User: El password debe tener un minimo de 8 caracteres")
    private String pass;

}
