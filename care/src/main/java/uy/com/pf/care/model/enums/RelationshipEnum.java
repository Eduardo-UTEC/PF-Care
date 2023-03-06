package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum RelationshipEnum {
    NOBODY("No posee"),
    FATHER("Padre"),
    MOTHER("Madre"),
    BROTHER("Hermano"),
    SISTER("Hermana"),
    SON("Hijo"),
    DAUGHTER("Hija"),
    NEPHEW("Sobrino"),
    NIECE("Sobrina"),
    GRANDSON("Nieto"),
    GRANDDAUGHTER("Nieta"),
    UNCLE("Tio"),
    AUNT("Tia"),
    FRIEND("Amigo"),
    NEIGHBOR("Vecino"),
    COWORKER("Compañero de Trabajo");

    private String name;
}
