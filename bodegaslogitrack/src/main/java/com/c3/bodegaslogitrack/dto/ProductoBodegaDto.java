package com.c3.bodegaslogitrack.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBodegaDto {
    private Long productoId;

    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    private Double precio;

    @Size(max = 50, message = "La categoría no puede tener más de 50 caracteres")
    private String categoria;

    @Size(min = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
