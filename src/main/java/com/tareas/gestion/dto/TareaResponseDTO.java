package com.tareas.gestion.dto;

import java.time.LocalDateTime;

import com.tareas.gestion.model.EstadoTarea;
import com.tareas.gestion.model.PrioridadTarea;

import lombok.Data;

@Data
public class TareaResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private EstadoTarea estado;
    private PrioridadTarea prioridad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}