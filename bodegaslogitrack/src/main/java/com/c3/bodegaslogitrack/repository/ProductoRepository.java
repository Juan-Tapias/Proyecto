package com.c3.bodegaslogitrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.c3.bodegaslogitrack.entitie.Producto;



public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar por Categoria
    List<Producto> findByCategoria(String categoria);

    // Buscar por Stock
    List<Producto> findByStockLessThanEqual(Integer stock);

    // Buscar por Precio
    List<Producto> findByPrecioLessThanEqual(Double precio);
    
    @Modifying
    @Query("UPDATE Producto p SET p.stock = :nuevoStock WHERE p.id = :productoId")
    void actualizarStock(@Param("productoId") Long productoId, @Param("nuevoStock") Integer nuevoStock);

}
