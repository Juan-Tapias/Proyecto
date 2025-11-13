package com.c3.bodegaslogitrack.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.c3.bodegaslogitrack.dto.MovimientoDTO;
import com.c3.bodegaslogitrack.dto.MovimientoResponseDTO;
import com.c3.bodegaslogitrack.repository.BodegaRepository;
import com.c3.bodegaslogitrack.repository.MovimientoRepository;
import com.c3.bodegaslogitrack.repository.ProductoRepository;
import com.c3.bodegaslogitrack.services.MovimientosServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientosController {

    private final MovimientosServices movimientosServices;
    private final BodegaRepository bodegaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;


    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();

        long totalBodegas = bodegaRepository.count();
        long totalProductos = productoRepository.count();

        LocalDate hoy = LocalDate.now();
        long movimientosHoy = movimientoRepository.countByFechaBetween(
            hoy.atStartOfDay(),
            hoy.plusDays(1).atStartOfDay()
        );

        resumen.put("totalBodegas", totalBodegas);
        resumen.put("totalProductos", totalProductos);
        resumen.put("movimientosHoy", movimientosHoy);

        return resumen;
    }

    @PostMapping
    public ResponseEntity<MovimientoDTO> crearMovimiento(@RequestBody MovimientoDTO dto) {
        MovimientoDTO creado = movimientosServices.crearMovimiento(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(movimientosServices.listarMovimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimiento(@PathVariable Long id) {
        MovimientoDTO movimiento = movimientosServices.obtenerPorId(id);
        return ResponseEntity.ok(movimiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> actualizarMovimiento(@PathVariable Long id,
                                                              @RequestBody MovimientoDTO dto) {
        MovimientoDTO actualizado = movimientosServices.actualizarMovimiento(id, dto);
        return ResponseEntity.ok(actualizado);
    }
    @GetMapping("/rango")
        public ResponseEntity<List<MovimientoResponseDTO>> obtenerPorRango(
                @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {

            List<MovimientoResponseDTO> movimientos = movimientosServices.buscarPorRangoFechas(inicio, fin);
            return ResponseEntity.ok(movimientos);
        }


    /** DELETE */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientosServices.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}
