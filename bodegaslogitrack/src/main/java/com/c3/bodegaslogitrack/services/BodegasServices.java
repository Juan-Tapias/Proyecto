package com.c3.bodegaslogitrack.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.BodegaDTO;
import com.c3.bodegaslogitrack.entitie.Bodega;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.exceptions.ResourceNotFoundException;
import com.c3.bodegaslogitrack.repository.BodegaRepository;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BodegasServices {

    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;

    //---------------------------------- Listar todas las bodegas 
    public List<BodegaDTO> listar(){
        return bodegaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    } 

    //----------------------------------- Buscar Bodegas por Id
    public BodegaDTO BuscarPorId(Long id){
        return bodegaRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada"));
            }

    //----------------------------------- Crear Bodegas
    public BodegaDTO crear(BodegaDTO dto){
        Bodega bodega = new Bodega();
        bodega.setNombre(dto.getNombre());
        bodega.setUbicacion(dto.getUbicacion());
        bodega.setCapacidad(dto.getCapacidad());
        
        if(dto.getEncargadoId() != null){
            Usuario encargardo = usuarioRepository.findById(dto.getEncargadoId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Encargado no encontrado"));
            bodega.setEncargado(encargardo);
        }
        bodegaRepository.save(bodega);
        return toDto(bodega);
    }

    //-------------------------------------Actualizar Bodegas
public BodegaDTO actualizar(Long id, BodegaDTO dto) {
    Bodega bodega = bodegaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada"));

    bodega.setNombre(dto.getNombre());
    bodega.setUbicacion(dto.getUbicacion());
    bodega.setCapacidad(dto.getCapacidad());

    if (dto.getEncargadoId() != null) {
        Usuario encargado = usuarioRepository.findById(dto.getEncargadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Encargado no encontrado"));
        bodega.setEncargado(encargado);
    }
    bodegaRepository.save(bodega);
    return toDto(bodega);
}

    //----------------------------------------Eliminar Bodega
    public void eliminar(Long id){
        if (!bodegaRepository.existsById(id)){
            throw new ResourceNotFoundException("Bodega no encontrada");
        }
        bodegaRepository.deleteById(id);
    }

    //-----------------------------------------Listar por ciudad
    public List<BodegaDTO> listarPorCiudad(String ciudad){
        return bodegaRepository.findByUbicacion(ciudad)
                .stream()
                .map(this::toDto)
                .toList();
    }

    //-----------------------------------------Listar por limite
    public List<BodegaDTO> listarPorCapacidad(Integer limite){
        return bodegaRepository.findByCapacidadLessThan(limite)
                .stream()
                .map(this::toDto)
                .toList();
    }

    //----------------------------------------Buscar por encargado
    public List<BodegaDTO> buscarPorEncargado(Long id) {
        Usuario encargado = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Encargado no encontrado"));
        List<Bodega> bodegas = bodegaRepository.findByEncargado(encargado);
        if (bodegas.isEmpty()) {
            throw new RuntimeException("No se encontraron bodegas para este encargado.");
        }
        return bodegas.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
    }

    //----------------------------------------Cambiar a DTO
    private BodegaDTO toDto(Bodega bodega){
        BodegaDTO dto = new BodegaDTO();
        dto.setNombre(bodega.getNombre());
        dto.setUbicacion(bodega.getUbicacion());
        dto.setCapacidad(bodega.getCapacidad());
        dto.setEncargadoId(bodega.getEncargado() == null ? null : bodega.getEncargado().getId());
        
        return dto;
    }

}