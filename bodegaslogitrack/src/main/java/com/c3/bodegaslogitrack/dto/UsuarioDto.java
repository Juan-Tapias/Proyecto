package com.c3.bodegaslogitrack.dto;

import com.c3.bodegaslogitrack.entitie.enums.Rol;

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
    @Size(max = 50, message = "El usuario no puede tener mas de 100 caracteres")
    private String username;

    @NotBlank(message = "La password no puede estar vacio")
    @Size(max = 255, message = "La password no puede tener mas de 255 caracteres")
    private String password;

    private Rol rol;

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(max = 50, message = "El nombre no puede tener mas de 100 caracteres")
    private String nombre;

    private Boolean activo;

}