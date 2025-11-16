package com.c3.bodegaslogitrack.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.services.ProductoService;

@RestController
@RequestMapping("/api/admin/productos")
public class AdminProductosController {

    private ProductoService productoService;

    
    public AdminProductosController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // =================================== GETTERS ================================
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.BuscarPorId(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoria));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> listarPorStock() {
        return ResponseEntity.ok(productoService.listarPorStock());
    }

    @GetMapping("/precio/{precio}")
    public ResponseEntity<List<ProductoDTO>> listarPorPrecio(@PathVariable Double precio) {
        return ResponseEntity.ok(productoService.listarPorPrecio(precio));
    }

    // ================================== POST ========================
    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO dto, @RequestParam Long usuarioId) {
        ProductoDTO nuevoProducto = productoService.crear(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // ================================== PUT =========================
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }

    // ================================= DELETE ======================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestParam Long usuarioId
    ) {
        productoService.eliminar(id, usuarioId);
        return ResponseEntity.ok("Producto eliminado");
    }

}
