package com.c3.bodegaslogitrack.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.MovimientoDTO;
import com.c3.bodegaslogitrack.dto.MovimientoResponseDTO;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.repository.BodegaRepository;
import com.c3.bodegaslogitrack.repository.MovimientoRepository;
import com.c3.bodegaslogitrack.repository.ProductoRepository;
import com.c3.bodegaslogitrack.services.MovimientosServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/empleado/movimientos")
@RequiredArgsConstructor
public class EmpleadoMovimientosController {

    private final MovimientosServices movimientosServices;
    private final BodegaRepository bodegaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarMovimientos(Authentication authentication) {
        var usuario = (Usuario) authentication.getPrincipal();
        List<MovimientoResponseDTO> movimientos = movimientosServices.listarMovimientosPorUsuario(usuario.getId());
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimiento(@PathVariable Long id,
                                                           Authentication authentication) {
        var usuario = (Usuario) authentication.getPrincipal();
        MovimientoDTO movimiento = movimientosServices.obtenerPorIdYUsuario(id, usuario.getId());
        return ResponseEntity.ok(movimiento);
    }

    @GetMapping("/rango")
    public ResponseEntity<List<MovimientoResponseDTO>> obtenerPorRango(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            Authentication authentication) {

        var usuario = (Usuario) authentication.getPrincipal();
        List<MovimientoResponseDTO> movimientos = movimientosServices.buscarPorRangoFechasYUsuario(inicio, fin, usuario.getId());
        return ResponseEntity.ok(movimientos);
    }
    @PostMapping
    public ResponseEntity<MovimientoDTO> crearMovimiento(
            @RequestParam Long usuarioId,
            @RequestBody MovimientoDTO dto) {

        MovimientoDTO response = movimientosServices.crearMovimiento(dto, usuarioId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/resumen")
    public Map<String, Object> obtenerResumen() {
        Map<String, Object> resumen = new HashMap<>();

        Long totalBodegas = bodegaRepository.count();
        Long totalProductos = productoRepository.count();

        LocalDate hoy = LocalDate.now();
        Long movimientosHoy = movimientoRepository.countByFechaBetween(
                hoy.atStartOfDay(),
                hoy.plusDays(1).atStartOfDay()
        );

        resumen.put("totalBodegas", totalBodegas);
        resumen.put("totalProductos", totalProductos);
        resumen.put("movimientosHoy", movimientosHoy);

        return resumen;
    }
}
