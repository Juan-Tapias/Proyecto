package com.c3.bodegaslogitrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.bodegaslogitrack.entitie.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
