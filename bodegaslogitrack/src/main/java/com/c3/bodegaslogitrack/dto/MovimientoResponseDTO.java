package com.c3.bodegaslogitrack.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovimientoResponseDTO {
    private Long id;

    @Size(max = 50, message = "El tipo de movimiento no puede tener más de 50 caracteres")
    private String tipoMovimiento;

    @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
    private String comentario;


    private LocalDateTime fecha;

    @Size(max = 100, message = "El nombre de usuario no puede tener más de 100 caracteres")
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
