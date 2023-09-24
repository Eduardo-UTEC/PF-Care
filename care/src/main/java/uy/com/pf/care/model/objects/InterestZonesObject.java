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
public class InterestZonesObject {

    @NotNull(message = "InterestZonesObject: El departamento no puede ser nulo")
    @NotEmpty(message = "InterestZonesObject: El departamento no puede ser vacío")
    private String departmentName;

    @Builder.Default
    @Valid
    private List<CityObject> cities = new ArrayList<>();
}
