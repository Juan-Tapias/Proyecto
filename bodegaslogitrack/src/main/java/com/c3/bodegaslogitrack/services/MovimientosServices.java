package com.c3.bodegaslogitrack.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.MovimientoDTO;
import com.c3.bodegaslogitrack.dto.MovimientoDetalleDTO;
import com.c3.bodegaslogitrack.entitie.Bodega;
import com.c3.bodegaslogitrack.entitie.Movimiento;
import com.c3.bodegaslogitrack.entitie.MovimientoDetalle;
import com.c3.bodegaslogitrack.entitie.Producto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.entitie.enums.TipoMovimiento;
import com.c3.bodegaslogitrack.exceptions.ResourceNotFoundException;
import com.c3.bodegaslogitrack.repository.BodegaRepository;
import com.c3.bodegaslogitrack.repository.MovimientoRepository;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientosServices {
    private final MovimientoRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;

    //---------------------------------- Listar todos los movimientos
    public List<MovimientoDTO> listar(){
        return movimientoRepository.findAll()
                                   .stream()
                                   .map(this::toDto)
                                   .toList();
    }

    //--------------------------------- Obtener por ID
    public MovimientoDTO obtenerPorId(Long id){
        Movimiento movimiento = movimientoRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        return toDto(movimiento);
    }

    //----------------------------------Eliminar movimiento
    // @Transactional
    // public void eliminarMovimiento(Long id){
    //     Movimiento movimiento = movimientoRepository.findById(id)
    //                             .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

    //         for (MovimientoDetalle det : movimiento.getDetalles()) {
    //         Producto p = det.getProducto();
    //         switch (movimiento.getTipo()) {
    //             case ENTRADA -> p.setStock(p.getStock() - det.getCantidad());
    //             case SALIDA -> p.setStock(p.getStock() + det.getCantidad());
    //             case TRANSFERENCIA -> {
    //             }
    //         }
    //         productoRepository.save(p);
    //     }

    //     movimientoRepository.delete(movimiento);

    // }

    //--------------------------------- Crear un movimiento
    // @Transactional
    // public MovimientoDTO crearMovimiento(MovimientoDTO dto, Long currentUserId) {

    //     Usuario usuario = usuarioRepository.findById(currentUserId)
    //             .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    //     Movimiento movimiento = new Movimiento();
    //     movimiento.setUsuario(usuario);
    //     movimiento.setTipo(dto.getTipoMovimiento());
    //     movimiento.setComentario(dto.getComentario());
    //     movimiento.setFecha(LocalDateTime.now());

    //     if (dto.getBodegaOrigenId() != null) {
    //         Bodega bodegaOrigen = bodegaRepository.findById(dto.getBodegaOrigenId())
    //                 .orElseThrow(() -> new RuntimeException("Bodega origen no encontrada"));
    //         movimiento.setBodegaOrigen(bodegaOrigen);
    //     }

    //     if (dto.getBodegaDestinoId() != null) {
    //         Bodega bodegaDestino = bodegaRepository.findById(dto.getBodegaDestinoId())
    //                 .orElseThrow(() -> new RuntimeException("Bodega destino no encontrada"));
    //         movimiento.setBodegaDestino(bodegaDestino);
    //     }

    //     List<MovimientoDetalle> detalles = dto.getDetalles().stream().map(detDTO -> {
    //         Producto producto = productoRepository.findById(detDTO.getProductoId())
    //                 .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    //         if ((dto.getTipoMovimiento() == TipoMovimiento.SALIDA
    //                 || dto.getTipoMovimiento() == TipoMovimiento.TRANSFERENCIA)
    //                 && producto.getStock() < detDTO.getCantidad()) {
    //             throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
    //         }

    //         switch (dto.getTipoMovimiento()) {
    //             case ENTRADA:
    //                 producto.setStock(producto.getStock() + detDTO.getCantidad());
    //                 break;
    //             case SALIDA:
    //                 producto.setStock(producto.getStock() - detDTO.getCantidad());
    //                 break;
    //             case TRANSFERENCIA:
    //                 producto.setStock(producto.getStock()); 
    //                 break;
    //         }
    //         productoRepository.save(producto);

    //         // Crear detalle
    //         MovimientoDetalle detalle = new MovimientoDetalle();
    //         detalle.setProducto(producto);
    //         detalle.setCantidad(detDTO.getCantidad());
    //         detalle.setMovimiento(movimiento);
    //         return detalle;
    //     }).toList();

    //     movimiento.setDetalles(detalles);

    //     // 5️⃣ Guardar movimiento con detalles
    //     Movimiento guardado = movimientoRepository.save(movimiento);

    //     // 6️⃣ Convertir a DTO y devolver
    //     return toDto(guardado);
    // }



    private MovimientoDTO toDto(Movimiento movimiento) {
    MovimientoDTO dto = new MovimientoDTO();
    dto.setTipoMovimiento(movimiento.getTipo());
    dto.setComentario(movimiento.getComentario());
    dto.setBodegaOrigenId(movimiento.getBodegaOrigen() != null ? movimiento.getBodegaOrigen().getId() : null);
    dto.setBodegaDestinoId(movimiento.getBodegaDestino() != null ? movimiento.getBodegaDestino().getId() : null);

    List<MovimientoDetalleDTO> detalles = movimiento.getDetalles()
        .stream()
        .map(d -> {
            MovimientoDetalleDTO detDto = new MovimientoDetalleDTO();
            detDto.setProductoId(d.getProducto().getId());
            detDto.setCantidad(d.getCantidad());
            return detDto;
        })
        .toList();

    dto.setDetalles(detalles);
    return dto;
}

}
