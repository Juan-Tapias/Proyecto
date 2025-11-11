package com.c3.bodegaslogitrack.dto;

import java.util.List;

import com.c3.bodegaslogitrack.entitie.enums.TipoMovimiento;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDTO {
    
    @NotNull(message = "El tipo de movimiento en obligatorio")
    private TipoMovimiento tipoMovimiento;

    private String comentario;

    private Long bodegaOrigenId;
    private Long bodegaDestinoId;

    @NotNull(message = "Debe incluir detalles")
    private List<MovimientoDetalleDTO> detalles;
}
