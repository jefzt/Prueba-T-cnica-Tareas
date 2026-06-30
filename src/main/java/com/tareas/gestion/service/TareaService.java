package com.tareas.gestion.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tareas.gestion.dto.EstadisticasDTO;
import com.tareas.gestion.dto.TareaRequestDTO;
import com.tareas.gestion.dto.TareaResponseDTO;
import com.tareas.gestion.exception.RecursoNoEncontradoException;
import com.tareas.gestion.exception.TransicionEstadoInvalidaException;
import com.tareas.gestion.model.EstadoTarea;
import com.tareas.gestion.model.PrioridadTarea;
import com.tareas.gestion.model.Tarea;
import com.tareas.gestion.repository.TareaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;


    public List<TareaResponseDTO> listarTodas() {
        List<TareaResponseDTO> resultado = new ArrayList<>();
        for (Tarea tarea : tareaRepository.findAll()) {
            resultado.add(mapToDTO(tarea));
        }
        return resultado;
    }


    public TareaResponseDTO obtenerPorId(Long id) {
        Tarea tarea = buscarTareaOExcepcion(id);
        return mapToDTO(tarea);
    }

  
    public TareaResponseDTO crear(TareaRequestDTO request) {
        Tarea tarea = new Tarea();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setPrioridad(request.getPrioridad());
        tarea.setEstado(EstadoTarea.PENDIENTE);

        Tarea guardada = tareaRepository.save(tarea);
        return mapToDTO(guardada);
    }

    public TareaResponseDTO actualizar(Long id, TareaRequestDTO request) {
        Tarea tarea = buscarTareaOExcepcion(id);

        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setPrioridad(request.getPrioridad());

        Tarea actualizada = tareaRepository.save(tarea);
        return mapToDTO(actualizada);
    }

    public TareaResponseDTO cambiarEstado(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = buscarTareaOExcepcion(id);

        validarTransicionEstado(tarea.getEstado(), nuevoEstado);

        tarea.setEstado(nuevoEstado);
        Tarea actualizada = tareaRepository.save(tarea);
        return mapToDTO(actualizada);
    }


    public void eliminar(Long id) {
        Tarea tarea = buscarTareaOExcepcion(id);
        tareaRepository.delete(tarea);
    }

    public List<TareaResponseDTO> filtrarPorEstado(EstadoTarea estado) {
        List<TareaResponseDTO> resultado = new ArrayList<>();
        for (Tarea tarea : tareaRepository.findByEstado(estado)) {
            resultado.add(mapToDTO(tarea));
        }
        return resultado;
    }


    public List<TareaResponseDTO> filtrarPorPrioridad(PrioridadTarea prioridad) {
        List<TareaResponseDTO> resultado = new ArrayList<>();
        for (Tarea tarea : tareaRepository.findByPrioridad(prioridad)) {
            resultado.add(mapToDTO(tarea));
        }
        return resultado;
    }


    public List<TareaResponseDTO> buscarPorTitulo(String texto) {
        List<TareaResponseDTO> resultado = new ArrayList<>();
        for (Tarea tarea : tareaRepository.buscarPorTitulo(texto)) {
            resultado.add(mapToDTO(tarea));
        }
        return resultado;
    }

    /**
     * Nivel 2 - Opcion B: Estadisticas
     * Retorna el conteo de tareas agrupadas por estado y el total general.
     */
    public EstadisticasDTO obtenerEstadisticas() {
        EstadisticasDTO estadisticas = new EstadisticasDTO();

        Map<String, Long> conteo = new LinkedHashMap<>();
        long total = 0;

        for (EstadoTarea estado : EstadoTarea.values()) {
            long cantidad = tareaRepository.countByEstado(estado);
            conteo.put(estado.name(), cantidad);
            total += cantidad;
        }

        estadisticas.setConteoPorEstado(conteo);
        estadisticas.setTotalGeneral(total);
        return estadisticas;
    }

    
    private void validarTransicionEstado(EstadoTarea estadoActual, EstadoTarea nuevoEstado) {

        if (estadoActual == EstadoTarea.CANCELADA) {
            throw new TransicionEstadoInvalidaException(
                    "Una tarea en estado CANCELADA no puede cambiar de estado");
        }

        if (estadoActual == EstadoTarea.COMPLETADA
                && (nuevoEstado == EstadoTarea.PENDIENTE || nuevoEstado == EstadoTarea.EN_PROGRESO)) {
            throw new TransicionEstadoInvalidaException(
                    "Una tarea en estado COMPLETADA no puede volver a PENDIENTE ni EN_PROGRESO");
        }
    }

    private Tarea buscarTareaOExcepcion(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Tarea no encontrada con id: " + id));
    }

    private TareaResponseDTO mapToDTO(Tarea tarea) {
        TareaResponseDTO dto = new TareaResponseDTO();
        dto.setId(tarea.getId());
        dto.setTitulo(tarea.getTitulo());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaCreacion(tarea.getFechaCreacion());
        dto.setFechaActualizacion(tarea.getFechaActualizacion());
        return dto;
    }
}