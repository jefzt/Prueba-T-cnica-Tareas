package com.tareas.gestion.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tareas.gestion.dto.ApiResponse;
import com.tareas.gestion.dto.CambiarEstadoDTO;
import com.tareas.gestion.dto.EstadisticasDTO;
import com.tareas.gestion.dto.TareaRequestDTO;
import com.tareas.gestion.dto.TareaResponseDTO;
import com.tareas.gestion.model.EstadoTarea;
import com.tareas.gestion.model.PrioridadTarea;
import com.tareas.gestion.service.TareaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    /**
     * GET /api/tareas - Listar todas las tareas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> listarTodas() {
        List<TareaResponseDTO> tareas = tareaService.listarTodas();
        return ResponseEntity.ok(ApiResponse.ok("Tareas obtenidas exitosamente", tareas));
    }

    /**
     * GET /api/tareas/{id} - Obtener una tarea por id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        TareaResponseDTO tarea = tareaService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.ok("Tarea encontrada", tarea));
    }

    /**
     * POST /api/tareas - Crear una nueva tarea
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TareaResponseDTO>> crear(
            @Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO creada = tareaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tarea creada exitosamente", creada));
    }

    /**
     * PUT /api/tareas/{id} - Actualizar una tarea completa
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TareaRequestDTO request) {
        TareaResponseDTO actualizada = tareaService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Tarea actualizada exitosamente", actualizada));
    }

    /**
     * PATCH /api/tareas/{id}/estado - Cambiar solo el estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<TareaResponseDTO>> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoDTO request) {
        TareaResponseDTO actualizada = tareaService.cambiarEstado(id, request.getEstado());
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado exitosamente", actualizada));
    }

    /**
     * DELETE /api/tareas/{id} - Eliminar una tarea
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.ok("Tarea eliminada exitosamente", null));
    }

    /**
     * GET /api/tareas/filtrar/estado?estado=PENDIENTE
     */
    @GetMapping("/filtrar/estado")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> filtrarPorEstado(
            @RequestParam EstadoTarea estado) {
        List<TareaResponseDTO> tareas = tareaService.filtrarPorEstado(estado);
        return ResponseEntity.ok(ApiResponse.ok("Tareas filtradas por estado", tareas));
    }

    /**
     * GET /api/tareas/filtrar/prioridad?prioridad=ALTA
     */
    @GetMapping("/filtrar/prioridad")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> filtrarPorPrioridad(
            @RequestParam PrioridadTarea prioridad) {
        List<TareaResponseDTO> tareas = tareaService.filtrarPorPrioridad(prioridad);
        return ResponseEntity.ok(ApiResponse.ok("Tareas filtradas por prioridad", tareas));
    }

    /**
     * GET /api/tareas/buscar?q=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<TareaResponseDTO>>> buscarPorTitulo(
            @RequestParam String q) {
        List<TareaResponseDTO> tareas = tareaService.buscarPorTitulo(q);
        return ResponseEntity.ok(ApiResponse.ok("Resultados de busqueda", tareas));
    }

    /**
     * Nivel 2 - Opcion B: GET /api/tareas/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<EstadisticasDTO>> obtenerEstadisticas() {
        EstadisticasDTO estadisticas = tareaService.obtenerEstadisticas();
        return ResponseEntity.ok(ApiResponse.ok("Estadisticas obtenidas exitosamente", estadisticas));
    }
}