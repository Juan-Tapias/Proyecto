package com.c3.bodegaslogitrack.testSebastian;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.c3.bodegaslogitrack.entitie.Movimiento;

public interface ReporteExamenRepository extends JpaRepository<Movimiento, Long>{
    @Query("SELECT COUNT(m) FROM Movimiento m WHERE m.tipo = 'TRANSFERENCIA' ")
    Long contarTransferencia();

    @Query("SELECT COUNT(m) FROM Movimiento m WHERE m.tipo = 'ENTRADA'")
    Long contarEntrada();

    @Query("SELECT COUNT(m) FROM Movimiento m WHERE m.tipo = 'SALIDA'")
    Long contarSalida();
}
