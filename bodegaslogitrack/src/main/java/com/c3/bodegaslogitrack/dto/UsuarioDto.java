package com.c3.bodegaslogitrack.dto;

import com.c3.bodegaslogitrack.entitie.Rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    @NotBlank(message = "El usuario no puede estar vacio")
    @Size(max = 100, message = "El nombre no puede tener mas de 100 caracteres")
    private String username;

    private String password;
    private Rol rol;
    private String nombre;
    private Boolean activo;

}