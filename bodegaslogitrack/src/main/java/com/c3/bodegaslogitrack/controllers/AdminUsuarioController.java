package com.c3.bodegaslogitrack.controllers;

import com.c3.bodegaslogitrack.dto.UsuarioDto;
import com.c3.bodegaslogitrack.services.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // GET - Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        List<UsuarioDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // GET - Obtener usuario por username
    @GetMapping("/{username}")
    public ResponseEntity<UsuarioDto> obtenerUsuario(@PathVariable String username) {
        UsuarioDto usuario = usuarioService.buscarPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    // POST - Crear nuevo usuario
    @PostMapping
    public ResponseEntity<UsuarioDto> crearUsuario(@Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto nuevoUsuario = usuarioService.crearUsuario(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // PUT - Actualizar usuario por username
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> actualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // DELETE - Eliminar usuario por username (borrado lógico)
    @DeleteMapping("/{username}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username) {
        usuarioService.eliminarUsuario(username);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }
}