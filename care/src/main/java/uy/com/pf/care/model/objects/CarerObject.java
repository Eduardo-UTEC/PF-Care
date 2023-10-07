package uy.com.pf.care.model.objects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CarerObject {

    @NotNull(message = "CarerObject: El nombre del Cuidador no puede ser nulo")
    @NotEmpty(message = "CarerObject: El nombre del Cuidador no puede ser vacío")
    @Size(max = 30, message = "CarerObject: El nombre del Cuidador no puede exceder los 50 caracteres")
    private String name1;

    @NotNull(message = "CarerObject: El apellido del Cuidador no puede ser nulo")
    @NotEmpty(message = "CarerObject: El nombre del Cuidador no puede ser vacío")
    @Size(max = 30, message = "CarerObject: El nombre del Cuidador no puede exceder los 50 caracteres")
    private String surName1;

    // Dias y rangos horarios semanales disponibles para el cuidado.
    // Si el cuidador tiene disponibilidad 24x7, dayTimeRange=[]
    // Si se omite, se asume dayTimeRange=[]
    @Valid
    private List<DayTimeRangeObject> dayTimeRange = new ArrayList<>();

}

