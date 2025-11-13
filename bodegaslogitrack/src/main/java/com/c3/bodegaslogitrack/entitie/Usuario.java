package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.c3.bodegaslogitrack.entitie.enums.Rol;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo = true;

    @ToString.Exclude
    @OneToMany(mappedBy = "encargado", cascade = CascadeType.ALL)
    private Set<Bodega> bodegasEncargadas = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Movimiento> movimientos = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<Auditoria> auditorias = new HashSet<>();

    // Getters y setters
    public Set<Bodega> getBodegasEncargadas() { return bodegasEncargadas; }
    public void setBodegasEncargadas(Set<Bodega> bodegasEncargadas) { this.bodegasEncargadas = bodegasEncargadas; }

    public Set<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(Set<Movimiento> movimientos) { this.movimientos = movimientos; }

    public Set<Auditoria> getAuditorias() { return auditorias; }
    public void setAuditorias(Set<Auditoria> auditorias) { this.auditorias = auditorias; }

    // ===== Evita ConcurrentModificationException =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
