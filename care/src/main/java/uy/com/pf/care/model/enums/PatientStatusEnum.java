package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public enum PatientStatusEnum {
    TEA("Transitando Enfermedad Avanzada"),
    UPD("Ultimo Periodo de Vida");

    private String name;
}
