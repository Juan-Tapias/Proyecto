package com.c3.bodegaslogitrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.bodegaslogitrack.entitie.Bodega;
import com.c3.bodegaslogitrack.entitie.Usuario;

import java.util.List;


public interface BodegaRepository  extends JpaRepository<Bodega, Long>{
    // Buscar por ubicacion
    List<Bodega> findByUbicacion(String ubicacion);

    // Buscar por capacidad
    List<Bodega> findByCapacidadLessThan(Integer capacidad);

    // Buscar por encargado
    List<Bodega> findByEncargado(Usuario encargado);
}
