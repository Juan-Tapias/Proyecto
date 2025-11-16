package com.c3.bodegaslogitrack.repository;

import com.c3.bodegaslogitrack.entitie.BodegaProducto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, Long>{
    List<BodegaProducto> findByBodegaId(Long bodegaId);

    Long countByBodegaEncargadoId(Long usuarioId);

}
