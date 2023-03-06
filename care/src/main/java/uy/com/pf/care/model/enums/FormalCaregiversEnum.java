package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum FormalCaregiversEnum {
    SERVICE_OF_CAREGIVERS("Servicio de acompañantes"),
    NURSE("Enfermero"),
    QUALIFIED_CAREGIVER("Cuidador calificado");

    private String name;
}
