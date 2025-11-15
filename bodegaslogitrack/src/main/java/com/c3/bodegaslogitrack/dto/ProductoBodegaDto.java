package com.c3.bodegaslogitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBodegaDto {
    private Long productoId;
    private String nombre;
    private Double precio;
    private String categoria;
    private Integer stock;
}
