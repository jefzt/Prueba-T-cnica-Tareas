package com.tareas.gestion.dto;

import java.util.Map;

import lombok.Data;

@Data
public class EstadisticasDTO {
    private Map<String, Long> conteoPorEstado;
    private Long totalGeneral;
}