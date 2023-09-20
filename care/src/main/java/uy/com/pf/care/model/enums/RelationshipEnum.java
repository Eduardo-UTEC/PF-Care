package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Getter
public enum RelationshipEnum {
    NOBODY(0, "No posee"),
    FATHER(1, "Padre"),
    MOTHER(2, "Madre"),
    BROTHER(3, "Hermano"),
    SISTER(4, "Hermana"),
    SON(5, "Hijo"),
    DAUGHTER(6, "Hija"),
    NEPHEW(7, "Sobrino"),
    NIECE(8, "Sobrina"),
    GRANDSON(9, "Nieto"),
    GRANDDAUGHTER(10, "Nieta"),
    UNCLE(11, "Tio"),
    AUNT(12, "Tia"),
    FRIEND(13, "Amigo"),
    NEIGHBOR(14, "Vecino"),
    COWORKER(15, "Compañero de Trabajo");

    private final int ordinal;
    private final String name;
}
