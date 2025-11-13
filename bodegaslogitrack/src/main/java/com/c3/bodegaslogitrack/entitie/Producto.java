package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "activo")
    private Boolean activo = true;

    @ToString.Exclude
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<BodegaProducto> bodegas = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<MovimientoDetalle> movimientoDetalles = new ArrayList<>();

    // ===== Evita ConcurrentModificationException =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
