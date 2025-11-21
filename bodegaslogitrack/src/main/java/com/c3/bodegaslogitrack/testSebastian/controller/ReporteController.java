package com.c3.bodegaslogitrack.testSebastian.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.testSebastian.ReporteExamenRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/examen/reportes")
@RequiredArgsConstructor
public class ReporteController {
    private final ReporteExamenRepository reporteExamenRepository;

    @GetMapping("/movimientos")
    public Map<String, Object> reporteMovimientos() {
        Map<String, Object> resumen = new HashMap<>();

        
        Long totalTransferencia = reporteExamenRepository.contarTransferencia();
        Long totalSalida = reporteExamenRepository.contarSalida();

        Long totalEntrada = reporteExamenRepository.contarEntrada();

        resumen.put("total Entrada", totalEntrada);
        resumen.put("Total Salidas", totalSalida);
        resumen.put("Total Trannferencia", totalTransferencia);

        return resumen;
    }
    
}
