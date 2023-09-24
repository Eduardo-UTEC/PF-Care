package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityObject {

    @NotNull(message = "CityObject: la clave 'cityName' no puede ser nula")
    @NotEmpty(message = "CityObject: la clave 'cityName' no puede ser vacía")
    private String cityName;

    @Builder.Default
    @Valid
    private List<String> neighborhoodNames = new ArrayList<>();
}
