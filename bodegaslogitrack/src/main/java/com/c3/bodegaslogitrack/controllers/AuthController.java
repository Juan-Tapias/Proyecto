package com.c3.bodegaslogitrack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.UsuarioDto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.services.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping
public class AuthController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/api/register")
    public String registrar(@RequestBody UsuarioDto usuario) {
        usuarioService.registrar(usuario);
        return "Usuario registrado";
    }

    @PostMapping("/api/login")
    public String loginn(@RequestBody Usuario usuario) {
        boolean ok = usuarioService.login(usuario.getUsername(), usuario.getPassword());
        if (ok){
            return "Login exitoso";
        } else {
            return "Credenciales invalidas";
        }
    }
    
    
}
