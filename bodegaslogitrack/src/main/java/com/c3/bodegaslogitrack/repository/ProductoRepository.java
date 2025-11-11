package com.c3.bodegaslogitrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.bodegaslogitrack.entitie.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar por Categoria
    List<Producto> findByCategoria(String categoria);

    // Buscar por Stock
    List<Producto> findByStock(Integer stock);

    // Buscar por Precio
    List<Producto> findByPrecio(Double precio);
}
