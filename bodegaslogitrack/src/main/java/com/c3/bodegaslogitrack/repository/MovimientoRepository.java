package com.c3.bodegaslogitrack.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.bodegaslogitrack.entitie.Movimiento;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.entitie.enums.TipoMovimiento;


public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    //----------------------------- Buscar por un usuario
    List<Movimiento> findByUsuario(Usuario usuario);

    //-----------------------------  Buscar por tipos
    List<Movimiento> findByTipo(TipoMovimiento tipo);

    //----------------------------- Buscar por rango de fechas
    List<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
}
