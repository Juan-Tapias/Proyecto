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

    @ToString.Exclude
    @OneToMany(mappedBy = "bodega", cascade = CascadeType.ALL)
    private List<BodegaProducto> productos = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bodegaOrigen", cascade = CascadeType.ALL)
    private List<Movimiento> movimientosOrigen = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "bodegaDestino", cascade = CascadeType.ALL)
    private List<Movimiento> movimientosDestino = new ArrayList<>();

    // ===== Evita ConcurrentModificationException =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bodega)) return false;
        Bodega bodega = (Bodega) o;
        return Objects.equals(id, bodega.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
