package com.c3.bodegaslogitrack.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoDetalleDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @Min(value = 1, message = "La cantidad debe ser mayor que 0")
    private int cantidad;
}
