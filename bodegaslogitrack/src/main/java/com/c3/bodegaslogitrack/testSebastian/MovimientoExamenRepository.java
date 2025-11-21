package com.c3.bodegaslogitrack.testSebastian;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.c3.bodegaslogitrack.entitie.Movimiento;

public interface MovimientoExamenRepository extends JpaRepository<Movimiento, Long> {
    
    @Modifying
    @Query("SELECT m FROM Movimiento m ORDER BY m.fecha DESC LIMIT 10")
    List<Movimiento> ListarUltimosDiezPorFechaDescendiente();
}
