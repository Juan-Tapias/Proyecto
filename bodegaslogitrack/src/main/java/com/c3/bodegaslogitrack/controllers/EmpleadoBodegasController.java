package com.c3.bodegaslogitrack.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.c3.bodegaslogitrack.dto.BodegaDTO;
import com.c3.bodegaslogitrack.services.BodegasServices;

import java.util.List;

@RestController
@RequestMapping("api/empleado/bodegas")
public class EmpleadoBodegasController {

    private final BodegasServices bodegasServices;

    public EmpleadoBodegasController(BodegasServices bodegasServices) {
        this.bodegasServices = bodegasServices;
    }

    // ==================== GET =======================

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

    @GetMapping("/encargado/{id}")
    public ResponseEntity<List<BodegaDTO>> getByEncargado(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.buscarPorEncargado(id));
    }

    @GetMapping("/ubicacion/{ciudad}")
    public ResponseEntity<List<BodegaDTO>> getByCiudad(
            @PathVariable String ciudad,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.listarPorCiudad(ciudad));
    }

    @GetMapping("/capacidad/{limite}")
    public ResponseEntity<List<BodegaDTO>> getByLimite(
            @PathVariable Integer limite,
            @RequestParam Long usuarioId) {

        return ResponseEntity.ok(bodegasServices.listarPorCapacidad(limite));
    }
}
