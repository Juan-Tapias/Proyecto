package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;

@Entity
@Table(name = "bodega_producto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bodega_id", "producto_id"})
})
public class BodegaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    // Constructores
    public BodegaProducto() {}

    public BodegaProducto(Bodega bodega, Producto producto, Integer stock) {
        this.bodega = bodega;
        this.producto = producto;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bodega getBodega() { return bodega; }
    public void setBodega(Bodega bodega) { this.bodega = bodega; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}