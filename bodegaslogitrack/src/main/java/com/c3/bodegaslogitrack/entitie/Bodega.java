package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bodega")
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

    // Constructores
    public Bodega() {}

    public Bodega(String nombre, String ubicacion, Integer capacidad, Usuario encargado) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidad = capacidad;
        this.encargado = encargado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public Usuario getEncargado() { return encargado; }
    public void setEncargado(Usuario encargado) { this.encargado = encargado; }

    public List<BodegaProducto> getProductos() { return productos; }
    public void setProductos(List<BodegaProducto> productos) { this.productos = productos; }

    public List<Movimiento> getMovimientosOrigen() { return movimientosOrigen; }
    public void setMovimientosOrigen(List<Movimiento> movimientosOrigen) { this.movimientosOrigen = movimientosOrigen; }

    public List<Movimiento> getMovimientosDestino() { return movimientosDestino; }
    public void setMovimientosDestino(List<Movimiento> movimientosDestino) { this.movimientosDestino = movimientosDestino; }
}