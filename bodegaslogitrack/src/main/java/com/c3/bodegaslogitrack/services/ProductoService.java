package com.c3.bodegaslogitrack.services;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c3.bodegaslogitrack.dto.ProductoDTO;
import com.c3.bodegaslogitrack.entitie.Producto;
import com.c3.bodegaslogitrack.entitie.Usuario;
import com.c3.bodegaslogitrack.exceptions.ResourceNotFoundException;
import com.c3.bodegaslogitrack.repository.ProductoRepository;
import com.c3.bodegaslogitrack.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service

public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

     @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @PersistenceContext
    private EntityManager em; 


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
    //----------------------------- Crear Producto
    @Transactional
    public ProductoDTO crear(ProductoDTO dto, Long usuarioId) {

        em.createNativeQuery("SET @current_user_id = :userId")
          .setParameter("userId", usuarioId)
          .executeUpdate();

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());

        productoRepository.save(producto);

        return toDto(producto);
    }


     @Transactional
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        // 1️⃣ Validar que el producto exista
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        // 2️⃣ Validar que el usuario exista (si viene en el DTO)
        if (dto.getUsuarioId() == null) {
            throw new RuntimeException("UsuarioId es obligatorio para la auditoría");
        }
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3️⃣ Setear la variable de sesión para que el trigger la use
        entityManager.createNativeQuery("SET @current_user_id = :userId")
                .setParameter("userId", usuario.getId())
                .executeUpdate();

        // 4️⃣ Actualizar los campos del producto
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setActivo(dto.getActivo());

        // 5️⃣ Guardar
        productoRepository.save(producto);

        // 6️⃣ Retornar DTO actualizado
        return toDto(producto);
    }
    // ----------------------------- Eliminar Producto
    @Transactional
    public void eliminar(Long id, Long usuarioId) {

        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        entityManager.createNativeQuery("SET @current_user_id = :userId")
                .setParameter("userId", usuario.getId())
                .executeUpdate();

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
        dto.setId(producto.getId());                         
        dto.setNombre(producto.getNombre());
        dto.setCategoria(producto.getCategoria());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setActivo(producto.getActivo());
        
        return dto;

    }
}

