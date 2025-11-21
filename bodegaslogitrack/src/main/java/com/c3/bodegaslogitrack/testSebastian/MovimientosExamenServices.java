package com.c3.bodegaslogitrack.testSebastian;

import java.util.List;

import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.MovimientoResponseDTO;
import com.c3.bodegaslogitrack.entitie.Movimiento;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovimientosExamenServices {
    private final MovimientoExamenRepository movimientoExamenRepository;

    public List<MovimientoResponseDTO> BuscarUltimosDiez(){
        List<Movimiento> movimientos = movimientoExamenRepository.ListarUltimosDiezPorFechaDescendiente();

        return movimientos.stream().map(mov -> {
            MovimientoResponseDTO dto = new MovimientoResponseDTO();
            dto.setId(mov.getId());
            dto.setTipoMovimiento(mov.getTipo().name());;
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
}
