package com.c3.bodegaslogitrack.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Objects;

@Entity
@Table(name = "bodega_producto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"bodega_id", "producto_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BodegaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id", nullable = false)
    private Bodega bodega;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    public BodegaProducto(Bodega bodega, Producto producto, Integer stock) {
        this.bodega = bodega;
        this.producto = producto;
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BodegaProducto)) return false;
        BodegaProducto that = (BodegaProducto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
