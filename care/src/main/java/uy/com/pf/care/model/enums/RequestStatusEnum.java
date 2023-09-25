package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatusEnum {
    STARTED(0, "Iniciado"),
    IN_PROCESS(1, "En proceso"),
    APPROVED(2, "Aprobado"),
    CANCELED(4, "Cancelado"),
    DELIVERED(5, "Entregado"),
    DENIED(6, "Rechazado");

    private final int ordinal;
    private final String name;
}
