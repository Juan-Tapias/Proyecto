package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bodega")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bodega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "ubicacion", nullable = false, length = 255)
    private String ubicacion;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encargado_id", nullable = false)
    private Usuario encargado;

    @OneToMany(mappedBy = "bodega", cascade = CascadeType.ALL)
    private List<BodegaProducto> productos = new ArrayList<>();

    @OneToMany(mappedBy = "bodegaOrigen", cascade = CascadeType.ALL)
    private List<Movimiento> movimientosOrigen = new ArrayList<>();

    @OneToMany(mappedBy = "bodegaDestino", cascade = CascadeType.ALL)
    private List<Movimiento> movimientosDestino = new ArrayList<>();

}