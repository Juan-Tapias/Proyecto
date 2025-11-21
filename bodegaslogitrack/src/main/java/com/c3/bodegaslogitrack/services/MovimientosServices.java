package com.c3.bodegaslogitrack.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.c3.bodegaslogitrack.dto.MovimientoDTO;
import com.c3.bodegaslogitrack.dto.MovimientoDetalleDTO;
import com.c3.bodegaslogitrack.dto.MovimientoResponseDTO;
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
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public MovimientoDTO crearMovimiento(MovimientoDTO dto, Long usuarioId) {

        em.createNativeQuery("SET @current_user_id = :userId")
                .setParameter("userId", usuarioId)
                .executeUpdate();

        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(dto.getTipoMovimiento());
        movimiento.setUsuario(usuario);
        movimiento.setComentario(dto.getComentario());
        movimiento.setFecha(LocalDateTime.now());

        switch (dto.getTipoMovimiento()) {

            case ENTRADA -> {
                if (dto.getBodegaDestinoId() == null) {
                    throw new ResourceNotFoundException("La ENTRADA requiere bodegaDestinoId");
                }
                Bodega destino = bodegaRepository.findById(dto.getBodegaDestinoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
                movimiento.setBodegaDestino(destino);
            }

            case SALIDA -> {
                if (dto.getBodegaOrigenId() == null) {
                    throw new ResourceNotFoundException("La SALIDA requiere bodegaOrigenId");
                }
                Bodega origen = bodegaRepository.findById(dto.getBodegaOrigenId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
                movimiento.setBodegaOrigen(origen);
            }

            case TRANSFERENCIA -> {
                if (dto.getBodegaOrigenId() == null || dto.getBodegaDestinoId() == null) {
                    throw new ResourceNotFoundException("La TRANSFERENCIA requiere bodegaOrigenId y bodegaDestinoId");
                }
                Bodega origen = bodegaRepository.findById(dto.getBodegaOrigenId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega origen no encontrada"));
                Bodega destino = bodegaRepository.findById(dto.getBodegaDestinoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Bodega destino no encontrada"));
                movimiento.setBodegaOrigen(origen);
                movimiento.setBodegaDestino(destino);
            }
        }

        // 4️⃣ Procesar detalles
        Set<MovimientoDetalle> detallesSet = new HashSet<>();

        for (MovimientoDetalleDTO detDTO : dto.getDetalles()) {

            Producto producto = productoRepository.findById(detDTO.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            // Validación de stock en SALIDA y TRANSFERENCIA
            if ((dto.getTipoMovimiento() == TipoMovimiento.SALIDA ||
                dto.getTipoMovimiento() == TipoMovimiento.TRANSFERENCIA)
                && producto.getStock() < detDTO.getCantidad()) {

                throw new ResourceNotFoundException("Stock insuficiente: " + producto.getNombre());
            }

            // Calcular nuevo stock
            Integer nuevoStock = switch (dto.getTipoMovimiento()) {
                case ENTRADA -> producto.getStock() + detDTO.getCantidad();
                case SALIDA -> producto.getStock() - detDTO.getCantidad();
                case TRANSFERENCIA -> producto.getStock(); // En Transferencia no se cambia stock aquí
            };

            // Actualizar stock
            producto.setStock(nuevoStock);
            productoRepository.save(producto);  // 🔥 TRIGGER DE PRODUCTO SE EJECUTA AQUÍ

            // Crear detalle
            MovimientoDetalle detalle = new MovimientoDetalle();
            detalle.setProducto(producto);
            detalle.setCantidad(detDTO.getCantidad());
            detalle.setMovimiento(movimiento);

            detallesSet.add(detalle);
        }

        movimiento.setDetalles(detallesSet);

        // 5️⃣ Guardar movimiento final
        Movimiento guardado = movimientoRepository.save(movimiento);

        // 6️⃣ Retornar DTO
        return toDto(guardado);
    }




    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarMovimientos() {
        List<Movimiento> movimientos = movimientoRepository.findAllWithRelations();
        System.out.println("Movimientos encontrados: " + movimientos.size());

        return movimientoRepository.findAll().stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().toString());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());
            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(
                    mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null
            );
            dto.setBodegaDestinoNombre(
                    mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null
            );

            dto.setDetalles(
                    mov.getDetalles().stream().map(det -> {
                        MovimientoResponseDTO.DetalleDTO detDto = new MovimientoResponseDTO.DetalleDTO();
                        detDto.setProductoNombre(det.getProducto().getNombre());
                        detDto.setCantidad(det.getCantidad());
                        return detDto;
                    }).collect(Collectors.toList())
            );

            return dto;
        }).collect(Collectors.toList());
    }

    public MovimientoDTO obtenerPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        return toDto(movimiento);
    }

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

    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));
        movimientoRepository.delete(movimiento);
    }
    
   
    public List<MovimientoResponseDTO> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        List<Movimiento> movimientos = movimientoRepository.findByFechaBetween(inicio, fin);

        return movimientos.stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();

            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().name());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());

            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(
                mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null
            );
            dto.setBodegaDestinoNombre(
                mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null
            );
            List<MovimientoResponseDTO.DetalleDTO> detalles = mov.getDetalles().stream().map(det -> {
                MovimientoResponseDTO.DetalleDTO detalleDTO = new MovimientoResponseDTO.DetalleDTO();
                detalleDTO.setProductoNombre(det.getProducto().getNombre());
                detalleDTO.setCantidad(det.getCantidad());
                return detalleDTO;
            }).toList();

            dto.setDetalles(detalles);

            return dto;
        }).toList();
    }

        // Listar movimientos de un usuario
    public List<MovimientoResponseDTO> listarMovimientosPorUsuario(Long usuarioId) {
        List<Movimiento> movimientos = movimientoRepository.findByUsuario_Id(usuarioId);

        return movimientos.stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().name());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());
            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null);
            dto.setBodegaDestinoNombre(mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null);

            List<MovimientoResponseDTO.DetalleDTO> detalles = mov.getDetalles().stream().map(det -> {
                MovimientoResponseDTO.DetalleDTO detalleDTO = new MovimientoResponseDTO.DetalleDTO();
                detalleDTO.setProductoNombre(det.getProducto().getNombre());
                detalleDTO.setCantidad(det.getCantidad());
                return detalleDTO;
            }).toList();

            dto.setDetalles(detalles);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<MovimientoResponseDTO> listarRecientes() {
        List<Movimiento> movimientos = movimientoRepository.findAllWithRelations();
        System.out.println("Movimientos encontrados: " + movimientos.size());

        return movimientoRepository.findAll().stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().toString());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());
            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(
                    mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null
            );
            dto.setBodegaDestinoNombre(
                    mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null
            );

            dto.setDetalles(
                    mov.getDetalles().stream().map(det -> {
                        MovimientoResponseDTO.DetalleDTO detDto = new MovimientoResponseDTO.DetalleDTO();
                        detDto.setProductoNombre(det.getProducto().getNombre());
                        detDto.setCantidad(det.getCantidad());
                        return detDto;
                    }).collect(Collectors.toList())
            );

            return dto;
        }).collect(Collectors.toList());
    }

    // Obtener movimiento por id y usuario
    public MovimientoDTO obtenerPorIdYUsuario(Long movimientoId, Long usuarioId) {
        Movimiento movimiento = movimientoRepository.findByIdAndUsuario_Id(movimientoId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
        return toDto(movimiento);
    }

    // Buscar movimientos por rango de fechas y usuario
    public List<MovimientoResponseDTO> buscarPorRangoFechasYUsuario(LocalDateTime inicio, LocalDateTime fin, Long usuarioId) {
        List<Movimiento> movimientos = movimientoRepository.findByFechaBetweenAndUsuario_Id(inicio, fin, usuarioId);

        return movimientos.stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().name());
            dto.setComentario(mov.getComentario());
            dto.setFecha(mov.getFecha());
            dto.setUsuarioNombre(mov.getUsuario().getNombre());
            dto.setBodegaOrigenNombre(mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null);
            dto.setBodegaDestinoNombre(mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null);

            List<MovimientoResponseDTO.DetalleDTO> detalles = mov.getDetalles().stream().map(det -> {
                MovimientoResponseDTO.DetalleDTO detalleDTO = new MovimientoResponseDTO.DetalleDTO();
                detalleDTO.setProductoNombre(det.getProducto().getNombre());
                detalleDTO.setCantidad(det.getCantidad());
                return detalleDTO;
            }).toList();

            dto.setDetalles(detalles);
            return dto;
        }).collect(Collectors.toList());
    }

    
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
