package com.tareas.gestion.dto;

import com.tareas.gestion.model.PrioridadTarea;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TareaRequestDTO {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(min = 3, message = "El titulo debe tener al menos 3 caracteres")
    private String titulo;

    private String descripcion;

    @NotNull(message = "La prioridad es obligatoria")
    private PrioridadTarea prioridad;
}