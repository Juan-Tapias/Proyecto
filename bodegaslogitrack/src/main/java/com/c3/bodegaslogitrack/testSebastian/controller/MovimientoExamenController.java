package com.c3.bodegaslogitrack.testSebastian.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.MovimientoResponseDTO;
import com.c3.bodegaslogitrack.testSebastian.MovimientosExamenServices;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/examen/movimientos")
@RequiredArgsConstructor
public class MovimientoExamenController {
    private final MovimientosExamenServices movimientosExamenServices;

    @GetMapping("/recientes")
    public ResponseEntity<List<MovimientoResponseDTO>> listarRecientes () {
        return ResponseEntity.ok(movimientosExamenServices.BuscarUltimosDiez());
    }

    
}
