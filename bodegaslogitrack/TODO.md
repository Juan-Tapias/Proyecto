# TODO: Orden recomendado para desarrollar nuevas funcionalidades en Spring Boot

1. **Definir las Entidades (Entities)**
   - Crear las clases de entidad que representan las tablas de la base de datos.
   - Añadir relaciones (`@OneToMany`, `@ManyToOne`, etc.).
   - Configurar campos con anotaciones JPA (`@Id`, `@GeneratedValue`, `@Column`, etc.).

2. **Crear los Repositorios (Repositories)**
   - Interfaces que extienden `JpaRepository` o `CrudRepository`.
   - Definir métodos personalizados si es necesario (`findByX`, `findByYAndZ`, etc.).

3. **Definir los DTOs (Data Transfer Objects)**
   - Crear clases para transportar datos entre capa de servicio y controladores.
   - Incluir solo los campos necesarios, no toda la entidad.

4. **Implementar los Servicios (Services)**
   - Crear clases con la lógica de negocio.
   - Inyectar los repositorios y realizar operaciones.
   - Transformar entidades a DTOs y viceversa si aplica.

5. **Crear los Controladores (Controllers)**
   - Definir endpoints REST (`@RestController`).
   - Inyectar los servicios.
   - Mapear solicitudes y respuestas usando DTOs.

6. **Configurar Seguridad**
   - Configurar Spring Security, JWT, roles, etc.
   - Asegurarse de proteger los endpoints sensibles.

10. **Documentación**
    - Añadir comentarios y documentación para otros desarrolladores.
    - Opcional: Swagger para API REST.

