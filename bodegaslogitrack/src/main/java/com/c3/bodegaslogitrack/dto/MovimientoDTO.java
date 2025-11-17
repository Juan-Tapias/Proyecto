package com.c3.bodegaslogitrack.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.c3.bodegaslogitrack.entitie.enums.TipoMovimiento;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {

    @JsonIgnore
    private Long usuarioId;

    private TipoMovimiento tipoMovimiento;

    @Size(max = 255, message = "El comentario no puede tener más de 255 caracteres")
    private String comentario;
    private Long bodegaOrigenId;
    private Long bodegaDestinoId;
    private LocalDateTime fecha = LocalDateTime.now();
    private List<MovimientoDetalleDTO> detalles;
}
