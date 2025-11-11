package com.c3.bodegaslogitrack.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.services.ProductoService;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // =================================== GETTERS ================================
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        List<ProductoDTO> productos = productoService.listar();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        ProductoDTO producto = productoService.BuscarPorId(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String categoria) {
        List<ProductoDTO> productos = productoService.listarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/stock/{stock}")
    public ResponseEntity<List<ProductoDTO>> listarPorStock(@PathVariable Integer stock) {
        List<ProductoDTO> productos = productoService.listarPorStock(stock);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/precio/{precio}")
    public ResponseEntity<List<ProductoDTO>> listarPorPrecio(@PathVariable Double precio) {
        List<ProductoDTO> productos = productoService.listarPorPrecio(precio);
        return ResponseEntity.ok(productos);
    }


    // ================================== POST =======================

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto) {
        ProductoDTO nuevoProducto = productoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // ================================== PUT ========================

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        ProductoDTO productoActualizado = productoService.actualizar(id, dto);
        return ResponseEntity.ok(productoActualizado);
    }

    // ================================= DELETE ======================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
