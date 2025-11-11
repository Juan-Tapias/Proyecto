package com.c3.bodegaslogitrack.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.c3.bodegaslogitrack.dto.BodegaDTO;
import com.c3.bodegaslogitrack.services.BodegasServices;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("api/bodegas")
public class BodegasController {

    private final BodegasServices bodegasServices;

    public BodegasController(BodegasServices bodegasServices) {
        this.bodegasServices = bodegasServices;
    }

    //=================================== GETTERS ================================

    //--------------------------------- Obtener todas las bodegas
    @GetMapping
    public ResponseEntity<List<BodegaDTO>> getAllBodegas() {
        List<BodegaDTO> bodegas = bodegasServices.listar();
        return ResponseEntity.ok(bodegas);
    }

    //--------------------------------- Obtener bodega por id
    @GetMapping("/{id}")
    public  ResponseEntity<BodegaDTO> getById(@PathVariable Long id) {
        BodegaDTO bodega = bodegasServices.BuscarPorId(id);
        return ResponseEntity.ok(bodega);
    }
    
    //--------------------------------- Obtener bodegas por encargado
    @GetMapping("/encargado/{id}")
    public ResponseEntity<List<BodegaDTO>> getByEncargado(@PathVariable Long id){
        List<BodegaDTO> bodegas = bodegasServices.buscarPorEncargado(id);
        return ResponseEntity.ok(bodegas);
    }
    
    //---------------------------------- Obtener bodegas por ubicacion
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<BodegaDTO>> getByCiudad(@PathVariable String ciudad) {
        List<BodegaDTO> bodegas = bodegasServices.listarPorCiudad(ciudad);
        return ResponseEntity.ok(bodegas);
    }

    //------------------------------------ Obtener bodegas por capacidad limite
    @GetMapping("/capacidad/{limite}")
    public ResponseEntity<List<BodegaDTO>> getByLimite(@PathVariable Integer limite) {
        List<BodegaDTO> bodegas = bodegasServices.listarPorCapacidad(limite);
        return ResponseEntity.ok(bodegas);
    }
    
    //=================================== POST ===========================

    //------------------------------ crear Bodega
    @PostMapping()
    public ResponseEntity<BodegaDTO> createBodega (@RequestBody BodegaDTO bodegaDTO) {
        BodegaDTO nuevaBodega = bodegasServices.crear(bodegaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaBodega);   
    }

    //=================================== PUT ==============================

    //--------------------------------- Actualizar una bodega
    @PutMapping("/{id}")
    public ResponseEntity<BodegaDTO> updateBodega (@PathVariable Long id, @RequestBody BodegaDTO bodegaDTO) {
        
        BodegaDTO bodegaActualizada = bodegasServices.actualizar(id, bodegaDTO);
        
        return ResponseEntity.ok(bodegaActualizada);
    }

    //=================================== DELETE ============================

    //---------------------------------- Eliminar una bodega
    @DeleteMapping("/{id}")
    public ResponseEntity<BodegaDTO> deleteBodega(@PathVariable Long id){
        bodegasServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
