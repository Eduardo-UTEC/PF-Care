package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.objects.AddressObject;

import java.time.LocalDate;

@Document("Residential")
@CompoundIndexes({
        @CompoundIndex(def =
                "{'countryName':1, " +
                        "'departmentName':1, " +
                        "'cityName':1, " +
                        "'name':1, " +
                        "'address.street':1, " +
                        "'address.portNumber':1}",
                unique = true
        )
})
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Residential {

    @Id
    private String residentialId;

    @NotNull(message = "Residential: El nombre del Residencial no puede ser nulo")
    @NotEmpty(message = "Residential: El nombre del Residencial no puede ser vacio")
    @Size(max = 40 , message = "Residential: El nombre del Residencial no puede exceder los 40 caracteres")
    private String name;

    @NotNull(message = "Residential: El telefono del Residencial no puede ser nulo")
    @NotEmpty(message = "Residential: El telefono del Residencial no puede ser vacio")
    @Size(max = 20 , message = "Residential: El telefono del Residencial no puede exceder los 20 caracteres")
    private String telephone;

    //Se toma al persistir.
    private LocalDate registrationDate;

    @BooleanFlag
    private Boolean deleted;

    @NotNull(message = "Residential: La ciudad del Residencial no puede ser nula")
    @NotEmpty(message = "Residential: La ciudad del Residencial no puede ser vacia")
    @Size(max = 25 , message = "Residential: La ciudad del Residencial no puede exceder los 25 caracteres")
    private String cityName;

    @NotNull(message = "Residential: El departamento del Residencial no puede ser nulo")
    @NotEmpty(message = "Residential: El departamento del Residencial no puede ser vacio")
    @Size(max = 25 , message = "Residential: El departamento del Residencial no puede exceder los 25 caracteres")
    private String departmentName;

    @NotNull(message = "Residential: El pais del Residencial no puede ser nulo")
    @NotEmpty(message = "Residential: El pais del Residencial no puede ser vacio")
    @Size(max = 15 , message = "Residential: El pais del Residencial no puede exceder los 15 caracteres")
    private String countryName;

}
