package com.c3.bodegaslogitrack.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.c3.bodegaslogitrack.dto.MovimientoDTO;
import com.c3.bodegaslogitrack.services.MovimientosServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {

    private final MovimientosServices movimientosServices;

    /** CREATE */
    @PostMapping
    public ResponseEntity<MovimientoDTO> crearMovimiento(@RequestBody MovimientoDTO dto) {
        MovimientoDTO creado = movimientosServices.crearMovimiento(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    /** READ ALL */
    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> listarMovimientos() {
        List<MovimientoDTO> lista = movimientosServices.listarMovimientos();
        return ResponseEntity.ok(lista);
    }

    /** READ BY ID */
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimiento(@PathVariable Long id) {
        MovimientoDTO movimiento = movimientosServices.obtenerPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    /** UPDATE */
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizarMovimiento(@PathVariable Long id,
                                                              @RequestBody MovimientoDTO dto) {
        MovimientoDTO actualizado = movimientosServices.actualizarMovimiento(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    /** DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientosServices.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
