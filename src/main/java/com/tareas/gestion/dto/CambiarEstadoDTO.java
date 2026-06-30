package com.tareas.gestion.dto;

import com.tareas.gestion.model.EstadoTarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoTarea estado;
}