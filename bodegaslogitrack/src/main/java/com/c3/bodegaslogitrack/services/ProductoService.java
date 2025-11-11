package com.c3.bodegaslogitrack.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.entitie.Producto;
import com.c3.bodegaslogitrack.repository.ProductoRepository;

@Service

public class ProductoService {
    
    private ProductoRepository productoRepository;

    // ----------------------------- Listar todos los productos
    public List<ProductoDTO> listar(){
        return productoRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ----------------------------- Buscar Producto por Id
    public ProductoDTO BuscarPorId(Long id){
        return productoRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            }
    
    // ----------------------------- Crear Producto
    public ProductoDTO crear(ProductoDTO dto){
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());
        productoRepository.save(producto);
        return toDto(producto);
    }

    // ----------------------------- Actualizar Producto
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());
        productoRepository.save(producto);
        return toDto(producto);
    }

    // ----------------------------- Eliminar Producto
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    // ---------------------------------- Listar productos por categoria
    public List<ProductoDTO> listarPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ---------------------------------- Listar productos por stock
    public List<ProductoDTO> listarPorStock(Integer stock) {
        return productoRepository.findByStock(stock)
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ---------------------------------- Listar productos por precio
    public List<ProductoDTO> listarPorPrecio(Double precio) {
        return productoRepository.findByPrecio(precio)
                .stream()
                .map(this::toDto)
                .toList();
    }
    //----------------------------------------Cambiar a DTO
    private ProductoDTO toDto(Producto producto){
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre(producto.getNombre());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setActivo(producto.getActivo());
        
        return dto;

    }
}

