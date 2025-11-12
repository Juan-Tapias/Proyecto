package com.c3.bodegaslogitrack.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.c3.bodegaslogitrack.repository.ProductoRepository;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientosServices {

    private final MovimientoRepository movimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;

    @PersistenceContext
    private EntityManager em; // <- Aquí inyectas el EntityManager

    @Transactional
    public MovimientoDTO crearMovimiento(MovimientoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Movimiento movimiento = new Movimiento();
        movimiento.setUsuario(usuario);
        movimiento.setTipo(dto.getTipoMovimiento());
        movimiento.setComentario(dto.getComentario());
        movimiento.setFecha(LocalDateTime.now());

        if (dto.getBodegaOrigenId() != null) {
            Bodega origen = bodegaRepository.findById(dto.getBodegaOrigenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
            movimiento.setBodegaOrigen(origen);
        }

        if (dto.getBodegaDestinoId() != null) {
            Bodega destino = bodegaRepository.findById(dto.getBodegaDestinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
            movimiento.setBodegaDestino(destino);
        }

        List<MovimientoDetalle> detalles = new ArrayList<>();
        for (MovimientoDetalleDTO detDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            // Validar stock si es SALIDA o TRANSFERENCIA
            if ((dto.getTipoMovimiento() == TipoMovimiento.SALIDA
                    || dto.getTipoMovimiento() == TipoMovimiento.TRANSFERENCIA)
                    && producto.getStock() < detDTO.getCantidad()) {
                throw new ResourceNotFoundException("Stock insuficiente: " + producto.getNombre());
            }

            int nuevoStock = switch (dto.getTipoMovimiento()) {
                case ENTRADA -> producto.getStock() + detDTO.getCantidad();
                case SALIDA -> producto.getStock() - detDTO.getCantidad();
                case TRANSFERENCIA -> producto.getStock();
            };

            // 🔹 Aquí seteas la variable de sesión antes de guardar
            em.createNativeQuery("SET @current_user_id = :userId")
              .setParameter("userId", usuario.getId())
              .executeUpdate();

            producto.setStock(nuevoStock);
            productoRepository.save(producto); 

            MovimientoDetalle detalle = new MovimientoDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(detDTO.getCantidad());
            detalle.setMovimiento(movimiento);
            detalles.add(detalle);
        }

        movimiento.setDetalles(detalles);
        Movimiento guardado = movimientoRepository.save(movimiento);

        return toDto(guardado);
    }

    /** READ ALL */
    public List<MovimientoDTO> listarMovimientos() {
        List<MovimientoDTO> listaDTO = new ArrayList<>();
        for (Movimiento m : movimientoRepository.findAll()) {
            listaDTO.add(toDto(m));
        }
        return listaDTO;
    }

    /** READ BY ID */
    public MovimientoDTO obtenerPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        return toDto(movimiento);
    }

    /** UPDATE */
    @Transactional
    public MovimientoDTO actualizarMovimiento(Long id, MovimientoDTO dto) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        if (dto.getComentario() != null) movimiento.setComentario(dto.getComentario());
        if (dto.getTipoMovimiento() != null) movimiento.setTipo(dto.getTipoMovimiento());

        if (dto.getBodegaOrigenId() != null) {
            Bodega origen = bodegaRepository.findById(dto.getBodegaOrigenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
            movimiento.setBodegaOrigen(origen);
        }

        if (dto.getBodegaDestinoId() != null) {
            Bodega destino = bodegaRepository.findById(dto.getBodegaDestinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
            movimiento.setBodegaDestino(destino);
        }

        return toDto(movimientoRepository.save(movimiento));
    }

    /** DELETE */
    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        movimientoRepository.delete(movimiento);
    }

    /** CONVERSIÓN ENTIDAD -> DTO */
    private MovimientoDTO toDto(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setUsuarioId(movimiento.getUsuario().getId());
        dto.setTipoMovimiento(movimiento.getTipo());
        dto.setComentario(movimiento.getComentario());
        dto.setBodegaOrigenId(movimiento.getBodegaOrigen() != null ? movimiento.getBodegaOrigen().getId() : null);
        dto.setBodegaDestinoId(movimiento.getBodegaDestino() != null ? movimiento.getBodegaDestino().getId() : null);

        List<MovimientoDetalleDTO> detallesDTO = new ArrayList<>();
        for (MovimientoDetalle det : movimiento.getDetalles()) {
            detallesDTO.add(new MovimientoDetalleDTO(det.getProducto().getId(), det.getCantidad()));
        }
        dto.setDetalles(detallesDTO);

        return dto;
    }
}
