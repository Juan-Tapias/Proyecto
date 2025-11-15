package com.c3.bodegaslogitrack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodegaDTO {

    private Long id; 


    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Size(max = 100, message = "La ubicación no puede tener más de 100 caracteres")
    private String ubicacion;

    @NotNull(message = "La capacidad no puede estar vacía")
    private Integer capacidad;

    @JsonIgnore
    private Long encargadoId;

    private String encargadoUserName;

    @JsonIgnore
    private Long usuarioId;
}
