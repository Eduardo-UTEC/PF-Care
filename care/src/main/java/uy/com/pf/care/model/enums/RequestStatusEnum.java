package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatusEnum {
    STARTED(0, "Iniciado"),
    IN_PROCESS(1, "En proceso"),
    APPROVED(2, "Aprobado"),
    DELIVERED(3, "Entregado"),
    DENIED(4, "Rechazado"),
    FINISHED(5, "Finalizado");

    private final int ordinal;
    private final String name;
}
