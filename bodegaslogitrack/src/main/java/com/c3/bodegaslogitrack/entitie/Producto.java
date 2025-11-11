package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "categoria", nullable = false, length = 50)
    private String categoria;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    @Column(name = "precio", nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "activo")
    private Boolean activo = true;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<BodegaProducto> bodegas = new ArrayList<>();

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<MovimientoDetalle> movimientoDetalles = new ArrayList<>();

    // Constructores
    public Producto() {}

    public Producto(String nombre, String categoria, BigDecimal precio) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<BodegaProducto> getBodegas() { return bodegas; }
    public void setBodegas(List<BodegaProducto> bodegas) { this.bodegas = bodegas; }

    public List<MovimientoDetalle> getMovimientoDetalles() { return movimientoDetalles; }
    public void setMovimientoDetalles(List<MovimientoDetalle> movimientoDetalles) { this.movimientoDetalles = movimientoDetalles; }
}