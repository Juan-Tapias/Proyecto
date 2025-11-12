package com.c3.bodegaslogitrack.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.entitie.Producto;
import com.c3.bodegaslogitrack.exceptions.ResourceNotFoundException;
import com.c3.bodegaslogitrack.repository.ProductoRepository;

@Service

public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

     @Autowired
    private JdbcTemplate jdbcTemplate;

    public void actualizarStockJdbc(Long productoId, int cantidad) {
        jdbcTemplate.update(
            "UPDATE producto SET stock = stock + ? WHERE id = ?",
            cantidad, productoId
        );
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
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
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
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
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    // ---------------------------------- Listar productos por categoria
    public List<ProductoDTO> listarPorCategoria(String categoria) {
        List<Producto> productos = productoRepository.findByCategoria(categoria);
        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron productos");
        }
        return productos
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ---------------------------------- Listar productos por stock
    public List<ProductoDTO> listarPorStock(Integer stock) {
        List<Producto> productos = productoRepository.findByStockLessThanEqual(stock);
        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron productos");
        }
        return productos
                .stream()
                .map(this::toDto)
                .toList();
    }

    // ---------------------------------- Listar productos por precio
    public List<ProductoDTO> listarPorPrecio(Double precio) {
        List<Producto> productos = productoRepository.findByPrecioLessThanEqual(precio);
        if (productos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron productos");
        }
        return productos.stream()
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

