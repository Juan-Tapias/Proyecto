package com.c3.bodegaslogitrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.c3.bodegaslogitrack.entitie.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findById(Long id);

    boolean existsByUsername(String username);
}
