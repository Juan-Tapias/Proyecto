package com.c3.bodegaslogitrack.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.UsuarioDto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.exceptions.ResourceNotFoundException;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean matchesMixed(String raw, String stored) {
        System.out.println("RAW: [" + raw + "]");
        System.out.println("STORED: [" + stored + "]");
        
        boolean isHash = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
        System.out.println("Is BCrypt? " + isHash);

        if (!isHash) {
            boolean eq = raw.equals(stored);
            System.out.println("Comparando texto plano => " + eq);
            return eq;
        }

        boolean match = passwordEncoder.matches(raw, stored);
        System.out.println("Comparando BCrypt => " + match);
        return match;
    }


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

            if (matchesMixed(password, usuario.getPassword())) {
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



    public Usuario loginEntity(String username, String password) {
    Optional<Usuario> usuarioOp = usuarioRepository.findByUsername(username);
    if (usuarioOp.isPresent()) {
        Usuario usuario = usuarioOp.get();
        if (passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
    }
    return null;
    }

    public List<UsuarioDto> listarTodos() {
    List<Usuario> usuarios = usuarioRepository.findAll();
    return usuarios.stream()
            .map(this::convertToResponseDto)
            .collect(Collectors.toList());
}

    public UsuarioDto buscarPorUsername(String username) {
    Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    return convertToResponseDto(usuario);
    }

    public UsuarioDto crearUsuario(UsuarioDto usuarioDto) {
    Usuario usuarioRegistrado = registrar(usuarioDto);
    return convertToResponseDto(usuarioRegistrado);
    }

    public UsuarioDto actualizarUsuario(Long id, UsuarioDto usuarioDto) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    if (!usuario.getUsername().equals(usuarioDto.getUsername()) && 
        usuarioRepository.existsByUsername(usuarioDto.getUsername())) {
        throw new ResourceNotFoundException("El username ya está en uso");
    }
    
    usuario.setUsername(usuarioDto.getUsername());
    usuario.setRol(usuarioDto.getRol());
    usuario.setNombre(usuarioDto.getNombre());
    usuario.setActivo(usuarioDto.getActivo());
    if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
        usuario.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
    }
    
    Usuario usuarioActualizado = usuarioRepository.save(usuario);
    return convertToResponseDto(usuarioActualizado);
    }

    public void eliminarUsuario(String username) {
    Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    
    usuarioRepository.delete(usuario);
    }

        public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

   private UsuarioDto convertToResponseDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setRol(usuario.getRol());
        dto.setNombre(usuario.getNombre());
        dto.setActivo(usuario.getActivo());
        dto.setCantidadBodegas(usuario.getBodegasEncargadas().size());
        return dto;
    }

}
