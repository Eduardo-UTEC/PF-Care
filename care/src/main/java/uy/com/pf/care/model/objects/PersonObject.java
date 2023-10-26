package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.PersonGenderEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonObject {

    @NotNull(message = "PersonObject: La clave 'name1' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'name1' no puede ser vacía")
    @Size(max = 30, message = "PersonObject: La clave 'name1' no puede exceder los 30 caracteres")
    private String name1;

    @NotNull(message = "PersonObject: La clave 'surname1' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'surname1' no puede ser vacía")
    @Size(max = 30, message = "PersonObject: La clave 'surname1' no puede exceder los 30 caracteres")
    private String surname1;

    @NotNull(message = "PersonObject: La clave 'gender' no puede ser nula")
    private PersonGenderEnum gender;

    @NotNull(message = "PersonObject: La clave 'address' no puede ser nula")
    private AddressObject address;

    @NotNull(message = "PersonObject: La clave 'telephone' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'telephone' no puede ser vacía")
    @Size(max = 30, message = "PersonObject: La clave 'telephone' debe contener un máximo de 30 caracteres")
    private String  telephone;

    //@NotNull(message = "PersonObject: La clave 'mail' no puede ser nula")
    //@NotEmpty(message = "PersonObject: La clave 'mail' no puede ser vacía")
    @Size(max = 80, message = "PersonObject: La clave 'mail' no puede exceder los 80 caracteres")
    private String mail;

    @NotNull(message = "PersonObject: La clave 'identificationDocument' no puede ser nula")
    @Positive(message = "PersonObject: La clave 'identificationDocument' debe contener dígitos")
    private Integer identificationDocument;

    @NotNull(message = "PersonObject: La clave 'dateBirth' no puede ser nula")
    private LocalDate dateBirth;

}
