package com.c3.bodegaslogitrack.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.services.ProductoService;

@RestController
@RequestMapping("/api/empleado/productos")
public class EmpleadoProductoController {

    private ProductoService productoService;

    public EmpleadoProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // =============================== GETTERS ================================
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar() {
        // Empleados solo pueden listar productos activos
        List<ProductoDTO> productos = productoService.listar();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.BuscarPorId(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDTO>> listarPorCategoria(@PathVariable String categoria) {
        List<ProductoDTO> productos = productoService.listarPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoDTO>> listarPorStock() {
        List<ProductoDTO> productos = productoService.listarPorStock();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/precio/{precio}")
    public ResponseEntity<List<ProductoDTO>> listarPorPrecio(@PathVariable Double precio) {
        List<ProductoDTO> productos = productoService.listarPorPrecio(precio);
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/crear")
    public ResponseEntity<ProductoDTO> crearProductoParaEmpleado(
            @RequestBody ProductoDTO productoDTO,
            @RequestParam Long usuarioId) {
        ProductoDTO nuevoProducto = productoService.crearProductoParaEmpleado(productoDTO, usuarioId);
        return ResponseEntity.ok(nuevoProducto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.ok(productoService.actualizar(id, dto));
    }
}
