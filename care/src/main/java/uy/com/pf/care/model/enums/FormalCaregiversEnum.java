package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormalCaregiversEnum {
    SERVICE_OF_CAREGIVERS(0, "Servicio de acompañantes"),
    NURSE(1, "Enfermero"),
    QUALIFIED_CAREGIVER(2, "Cuidador calificado");

    private final int ordinal;
    private final String name;
}
