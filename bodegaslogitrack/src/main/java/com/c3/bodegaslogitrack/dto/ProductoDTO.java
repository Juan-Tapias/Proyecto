package com.c3.bodegaslogitrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La categoria no puede estar vacia")
    @Size(max = 50, message = "La categoria no puede tener más de 50 caracteres")
    private String categoria;

    @NotNull(message = "El stock no puede ser nulo")
    private Integer stock;

    @NotNull(message = "El precio no puede ser nulo")
    private Double precio;

    private Boolean activo;
    private Long usuarioId; 

}
