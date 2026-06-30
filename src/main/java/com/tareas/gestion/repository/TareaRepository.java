package com.tareas.gestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tareas.gestion.model.EstadoTarea;
import com.tareas.gestion.model.PrioridadTarea;
import com.tareas.gestion.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByEstado(EstadoTarea estado);

    List<Tarea> findByPrioridad(PrioridadTarea prioridad);

    @Query("SELECT t FROM Tarea t WHERE LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Tarea> buscarPorTitulo(@Param("texto") String texto);

    long countByEstado(EstadoTarea estado);
}