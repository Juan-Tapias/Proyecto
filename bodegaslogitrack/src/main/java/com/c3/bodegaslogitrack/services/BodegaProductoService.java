package com.c3.bodegaslogitrack.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.ProductoBodegaDto;
import com.c3.bodegaslogitrack.entitie.Bodega;
import com.c3.bodegaslogitrack.entitie.BodegaProducto;
import com.c3.bodegaslogitrack.entitie.Producto;
import com.c3.bodegaslogitrack.repository.BodegaProductoRepository;
import com.c3.bodegaslogitrack.repository.BodegaRepository;
import com.c3.bodegaslogitrack.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BodegaProductoService {

    private final BodegaProductoRepository bodegaProductoRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;

    private ProductoBodegaDto toDto(BodegaProducto bp) {
        return new ProductoBodegaDto(
                bp.getProducto().getId(),
                bp.getProducto().getNombre(),
                bp.getProducto().getPrecio(),
                bp.getProducto().getCategoria(),
                bp.getStock()
        );
    }


    public List<ProductoBodegaDto> obtenerProductosPorBodega(Long bodegaId) {

        List<BodegaProducto> lista = bodegaProductoRepository.findByBodegaId(bodegaId);

        return lista.stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ProductoBodegaDto asignarProductoABodega(Long bodegaId, Long productoId, Integer cantidad) {

        Bodega bodega = bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new RuntimeException("Bodega no encontrada"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        BodegaProducto existente = bodegaProductoRepository
                .findByBodegaId(bodegaId)
                .stream()
                .filter(bp -> bp.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (existente == null) {
            existente = new BodegaProducto();
            existente.setBodega(bodega);
            existente.setProducto(producto);
            existente.setStock(cantidad);
        } else {
            // Sumar stock
            existente.setStock(existente.getStock() + cantidad);
        }

        BodegaProducto guardado = bodegaProductoRepository.save(existente);

        return toDto(guardado);
    }

    @Transactional
    public void reducirStock(Long bodegaId, Long productoId, Integer cantidad) {
        BodegaProducto bp = bodegaProductoRepository.findByBodegaId(bodegaId)
                .stream()
                .filter(x -> x.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("El producto no existe en esta bodega"));

        if (bp.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente en la bodega");
        }

        bp.setStock(bp.getStock() - cantidad);

        bodegaProductoRepository.save(bp);
    }
}
