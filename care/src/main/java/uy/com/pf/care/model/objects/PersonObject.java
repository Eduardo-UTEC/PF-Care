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
    @Size(max = 15, message = "PersonObject: La clave 'name1' no puede exceder los 15 caracteres")
    private String name1;

    @Size(max = 15, message = "PersonObject: La clave 'name2' no puede exceder los 15 caracteres")
    private String name2;

    @NotNull(message = "PersonObject: La clave 'surname1' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'surname1' no puede ser vacía")
    @Size(max = 15, message = "PersonObject: La clave 'surname1' no puede exceder los 15 caracteres")
    private String surname1;

    @Size(max = 15, message = "PersonObject: La clave 'surname2' no puede exceder los 15 caracteres")
    private String surname2;

    @NotNull(message = "PersonObject: La clave 'gender' no puede ser nula")
    private PersonGenderEnum gender;

    @NotNull(message = "PersonObject: La clave 'address' no puede ser nula")
    private AddressObject address;

    @NotNull(message = "PersonObject: La clave 'telephone' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'telephone' no puede ser vacía")
    @Size(max = 20, message = "PersonObject: La clave 'telephone' no puede exceder los 20 caracteres")
    private String  telephone;

    @NotNull(message = "PersonObject: La clave 'mail' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'mail' no puede ser vacía")
    @Size(max = 60, message = "PersonObject: La clave 'mail' no puede exceder los 60 caracteres")
    private String mail;

    @NotNull(message = "PersonObject: La clave 'identificationDocument' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'identificationDocument' no puede ser vacía")
    @Positive(message = "PersonObject: La clave 'identificationDocument' debe contener dígitos")
    private Integer identificationDocument;

    @NotNull(message = "PersonObject: La clave 'dateBirth' no puede ser nula")
    @NotEmpty(message = "PersonObject: La clave 'dateBirth' no puede ser vacía")
    private LocalDate dateBirth;
}
