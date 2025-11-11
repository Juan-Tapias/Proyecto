package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "encargado", cascade = CascadeType.ALL)
    private List<Bodega> bodegasEncargadas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Movimiento> movimientos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Auditoria> auditorias = new ArrayList<>();

    public List<Bodega> getBodegasEncargadas() { return bodegasEncargadas; }
    public void setBodegasEncargadas(List<Bodega> bodegasEncargadas) { this.bodegasEncargadas = bodegasEncargadas; }

    public List<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(List<Movimiento> movimientos) { this.movimientos = movimientos; }

    public List<Auditoria> getAuditorias() { return auditorias; }
    public void setAuditorias(List<Auditoria> auditorias) { this.auditorias = auditorias; }
}
