package com.c3.bodegaslogitrack.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.UsuarioDto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.security.JwtUtils;
import com.c3.bodegaslogitrack.services.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public String registrar(@RequestBody UsuarioDto usuario) {
        usuarioService.registrar(usuario);
        return "Usuario registrado";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDto usuario) {
        Usuario usuarioEncontrado = usuarioService.loginEntity(usuario.getUsername(), usuario.getPassword());
        if (usuarioEncontrado != null) {
            String token = jwtUtils.generarToken(
                    usuarioEncontrado.getUsername(),
                    usuarioEncontrado.getRol().name()
            );

            return ResponseEntity.ok(Map.of(
                    "id", usuarioEncontrado.getId(),
                    "token", token,
                    "rol", usuarioEncontrado.getRol().name(),
                    "username", usuarioEncontrado.getUsername()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales inválidas");
        }
    }
}
