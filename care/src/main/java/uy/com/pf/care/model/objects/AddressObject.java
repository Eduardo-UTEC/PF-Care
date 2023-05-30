package uy.com.pf.care.model.objects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressObject {

    @NotNull(message = "AddressObject: La clave 'street' no puede ser nula")
    @Size(max = 20 , message = "AddressObject: La calle no puede exceder los 20 caracteres")
    private String street;

    @NotNull(message = "AddressObject: La clave 'portNumber' no puede ser nula")
    private Integer portNumber;

    @NotNull(message = "AddressObject: La clave 'betweenStreet1' no puede ser nula")
    @Size(max = 20 , message = "AddressObject: La 'entre calle 1' no puede exceder los 20 caracteres")
    private String betweenStreet1;

    @NotNull(message = "AddressObject: La clave 'betweenStreet2' no puede ser nula")
    @Size(max = 20 , message = "AddressObject: La 'entre calle 2' no puede exceder los 20 caracteres")
    private String betweenStreet2;

    @NotNull(message = "AddressObject: La latitud y longitud  no pueden ser nulas")
    private Long lat, lon;
}
