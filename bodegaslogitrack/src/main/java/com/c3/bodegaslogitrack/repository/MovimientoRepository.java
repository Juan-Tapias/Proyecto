package com.c3.bodegaslogitrack.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.c3.bodegaslogitrack.entitie.Movimiento;
import com.c3.bodegaslogitrack.entitie.enums.TipoMovimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    // ----------------------------- Movimientos de un usuario (Empleado)
    List<Movimiento> findByUsuario_Id(Long usuarioId);

    // ----------------------------- Obtener un movimiento específico de un usuario
    Optional<Movimiento> findByIdAndUsuario_Id(Long id, Long usuarioId);

    // ----------------------------- Movimientos de un usuario en un rango de fechas
    List<Movimiento> findByFechaBetweenAndUsuario_Id(LocalDateTime inicio, LocalDateTime fin, Long usuarioId);

    // ----------------------------- Buscar movimientos por tipo
    List<Movimiento> findByTipo(TipoMovimiento tipo);

    // ----------------------------- Contar movimientos entre fechas
    Long countByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    Long countByUsuarioIdAndFechaBetween(Long usuarioId, LocalDateTime inicio, LocalDateTime fin);


    // ----------------------------- Listar movimientos entre fechas (sin filtrar por usuario, para Admin)
    List<Movimiento> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT m FROM Movimiento m " +
       "JOIN FETCH m.usuario u " +
       "LEFT JOIN FETCH u.bodegasEncargadas " +
       "LEFT JOIN FETCH m.bodegaOrigen " +
       "LEFT JOIN FETCH m.bodegaDestino " +
       "LEFT JOIN FETCH m.detalles d " +
       "LEFT JOIN FETCH d.producto")
    List<Movimiento> findAllWithRelations();

}
