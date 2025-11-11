package com.c3.bodegaslogitrack.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.UsuarioDto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrar(UsuarioDto dto){
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(dto.getRol());
        usuario.setNombre(dto.getNombre());
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public UsuarioDto login(String nombre, String password) {
        Optional<Usuario> usuarioOp = usuarioRepository.findByUsername(nombre);
        if (usuarioOp.isPresent()) {
            Usuario usuario = usuarioOp.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                UsuarioDto dto = new UsuarioDto();
                dto.setUsername(usuario.getUsername());
                dto.setNombre(usuario.getNombre());
                dto.setRol(usuario.getRol());
                dto.setActivo(usuario.getActivo());
                return dto;
            }
        }
        return null;
    }

}
