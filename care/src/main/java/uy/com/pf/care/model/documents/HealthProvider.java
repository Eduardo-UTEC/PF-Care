package uy.com.pf.care.model.documents;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("HealthProviders")
@CompoundIndexes({
        @CompoundIndex(
                def = "{'countryName':1, 'departmentName':1, 'cityName':1, 'name':1}",
                unique = true
        )
})
@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthProvider {
    @Id
    private String healthProviderId;

    @NotNull(message = "HealthProvider: La propiedad 'name' no puede ser nula")
    @NotEmpty(message = "HealthProvider: La propiedad 'name' no puede ser vacía")
    private String name;

    @NotNull(message = "HealthProvider: La propiedad 'deleted' no puede ser nula")
    @NotEmpty(message = "HealthProvider: La propiedad 'deleted' no puede ser vacía")
    @BooleanFlag
    private Boolean deleted;

    @NotNull(message = "HealthProvider: La propiedad 'cityName' no puede ser nula")
    @NotEmpty(message = "HealthProvider: La propiedad 'cityName' no puede ser vacía")
    private String cityName;

    @NotNull(message = "HealthProvider: La propiedad 'departmentName' no puede ser nula")
    @NotEmpty(message = "HealthProvider: La propiedad 'departmentName' no puede ser vacía")
    private String departmentName;

    @NotNull(message = "HealthProvider: La propiedad 'countryName' no puede ser nula")
    @NotEmpty(message = "HealthProvider: La propiedad 'countryName' no puede ser vacía")
    private String countryName;

}
