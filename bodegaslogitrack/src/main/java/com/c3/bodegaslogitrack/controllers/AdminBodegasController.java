package com.c3.bodegaslogitrack.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.c3.bodegaslogitrack.dto.BodegaDTO;
import com.c3.bodegaslogitrack.services.BodegasServices;

import java.util.List;

@RestController
@RequestMapping("api/admin/bodegas")
public class AdminBodegasController {

    private final BodegasServices bodegasServices;

    public AdminBodegasController(BodegasServices bodegasServices) {
        this.bodegasServices = bodegasServices;
    }

    // ==================== GET =======================
    
    @GetMapping
    public ResponseEntity<List<BodegaDTO>> getAll(
            @RequestParam Long usuarioId) {

        List<BodegaDTO> bodegas = bodegasServices.listar();
        return ResponseEntity.ok(bodegas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaDTO> getById(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        BodegaDTO dto = bodegasServices.BuscarPorId(id);
        return ResponseEntity.ok(dto);
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


    // ==================== POST =======================

    @PostMapping
    public ResponseEntity<BodegaDTO> create(
            @RequestBody BodegaDTO dto,
            @RequestParam Long usuarioId) {

        dto.setUsuarioId(usuarioId);
        BodegaDTO nueva = bodegasServices.crear(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }


    // ==================== PUT =======================

    @PutMapping("/{id}")
    public ResponseEntity<BodegaDTO> update(
            @PathVariable Long id,
            @RequestBody BodegaDTO dto,
            @RequestParam Long usuarioId) {

        dto.setUsuarioId(usuarioId);
        BodegaDTO updated = bodegasServices.actualizar(id, dto);

        return ResponseEntity.ok(updated);
    }


    // ==================== DELETE =======================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {

        bodegasServices.eliminar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
