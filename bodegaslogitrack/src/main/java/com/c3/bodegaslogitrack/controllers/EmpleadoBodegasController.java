package com.c3.bodegaslogitrack.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.c3.bodegaslogitrack.dto.BodegaDTO;
import com.c3.bodegaslogitrack.dto.ProductoBodegaDto;
import com.c3.bodegaslogitrack.services.BodegaProductoService;
import com.c3.bodegaslogitrack.services.BodegasServices;

import java.util.List;

@RestController
@RequestMapping("api/empleado/bodegas")
public class EmpleadoBodegasController {

    private final BodegasServices bodegasServices;
    private final BodegaProductoService bodegaProductoService;


    public EmpleadoBodegasController(BodegasServices bodegasServices, BodegaProductoService bodegaProductoService) {
        this.bodegasServices = bodegasServices;
        this.bodegaProductoService = bodegaProductoService;
    }

    @GetMapping
    public ResponseEntity<List<BodegaDTO>> getAll(
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaDTO> getById(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.BuscarPorId(id));
    }

    @GetMapping("/encargado")
    public ResponseEntity<List<BodegaDTO>> getByEncargado(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(bodegasServices.buscarPorEncargado(usuarioId));
    }
    @GetMapping("/{bodegaId}/productos")
    public List<ProductoBodegaDto> obtenerProductos(@PathVariable Long bodegaId) {
        return bodegaProductoService.obtenerProductosPorBodega(bodegaId);
    }

    @GetMapping("/capacidad/{limite}")
    public ResponseEntity<List<BodegaDTO>> getByLimite(
            @PathVariable Integer limite,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.listarPorCapacidad(limite));
    }
}
