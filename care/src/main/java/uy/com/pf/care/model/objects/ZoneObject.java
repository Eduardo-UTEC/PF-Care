package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneObject {

    @NotNull(message = "ZoneObject: El barrio no puede ser nulo")
    @NotEmpty(message = "ZoneObject: El barrio no puede ser vacio")
    @Size(max = 25 , message = "ZoneObject: El barrio no puede exceder los 25 caracteres")
    private String neighborhoodName;

    @NotNull(message = "ZoneObject: La ciudad no puede ser nula")
    @NotEmpty(message = "ZoneObject: La ciudad no puede ser vacia")
    @Size(max = 25 , message = "ZoneObject: La ciudad no puede exceder los 25 caracteres")
    private String cityName;

    @NotNull(message = "ZoneObject: El departamento no puede ser nulo")
    @NotEmpty(message = "ZoneObject: El departamento no puede ser vacio")
    @Size(max = 25 , message = "ZoneObject: El departamento no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "ZoneObject: El pais no puede ser nulo")
    @NotEmpty(message = "ZoneObject: El pais no puede ser vacio")
    @Size(max = 15 , message = "ZoneObject: El pais no puede exceder los 15 caracteres")
    private String countryName;
}
