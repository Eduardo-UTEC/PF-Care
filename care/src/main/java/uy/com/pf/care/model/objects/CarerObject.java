package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import uy.com.pf.care.model.enums.PersonGenderEnum;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CarerObject {

    @NotNull(message = "El nombre del Cuidador no puede ser nulo")
    @NotEmpty(message = "El nombre del Cuidador no puede ser vacío")
    @Size(max = 30, message = "El nombre del Cuidador no puede exceder los 50 caracteres")
    private String name1;

    @NotNull(message = "El apellido del Cuidador no puede ser nulo")
    @NotEmpty(message = "El nombre del Cuidador no puede ser vacío")
    @Size(max = 30, message = "El nombre del Cuidador no puede exceder los 50 caracteres")
    private String surname1;

    //gender puede ser nulo, en el caso de las empresas.
    private PersonGenderEnum gender;

    @NotNull(message = "La clave 'dateBirth' no puede ser nula")
    private LocalDate dateBirth;

    // Dias y rangos horarios semanales disponibles para el cuidado.
    // Si el cuidador tiene disponibilidad 24x7, dayTimeRange=[]
    // Si se omite, se asume dayTimeRange=[]
    @Valid
    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();

}

