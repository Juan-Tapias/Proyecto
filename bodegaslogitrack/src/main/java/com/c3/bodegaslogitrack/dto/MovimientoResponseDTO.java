package com.c3.bodegaslogitrack.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class MovimientoResponseDTO {
    private Long id;
    private String tipoMovimiento;
    private String comentario;
    private LocalDateTime fecha;
    private String usuarioNombre;
    private String bodegaOrigenNombre;
    private String bodegaDestinoNombre;
    private List<DetalleDTO> detalles;

    @Data
    public static class DetalleDTO {
        private String productoNombre;
        private int cantidad;
    }
}
