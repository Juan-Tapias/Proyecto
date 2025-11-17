-- Usuarios
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);


-- Bodegas
CREATE TABLE bodega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    capacidad INT NOT NULL,
    encargado_id INT NOT NULL,
    FOREIGN KEY (encargado_id) REFERENCES usuario(id)
);

-- Productos
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(12,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE bodega_producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bodega_id INT NOT NULL,
    producto_id INT NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    UNIQUE (bodega_id, producto_id),
    FOREIGN KEY (bodega_id) REFERENCES bodega(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Movimientos de Inventario
CREATE TABLE movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id INT NOT NULL,
    bodega_origen_id INT,
    bodega_destino_id INT,
    comentario VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (bodega_origen_id) REFERENCES bodega(id),
    FOREIGN KEY (bodega_destino_id) REFERENCES bodega(id)
);

-- Detalle de productos por Movimiento
CREATE TABLE movimiento_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    FOREIGN KEY (movimiento_id) REFERENCES movimiento(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

-- Auditoría genérica
CREATE TABLE auditoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    entidad VARCHAR(50) NOT NULL,
    entidad_id INT NOT NULL,
    tipo_operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    usuario_id INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valores_anteriores JSON,
    valores_nuevos JSON,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Triggers
DELIMITER $$

CREATE TRIGGER audit_producto_insert
AFTER INSERT ON producto
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (entidad, entidad_id, tipo_operacion, usuario_id, valores_nuevos)
    VALUES
    ('producto', NEW.id, 'INSERT', @current_user_id, 
        JSON_OBJECT('nombre', NEW.nombre, 'categoria', NEW.categoria, 'stock', NEW.stock, 'precio', NEW.precio));
END$$

CREATE TRIGGER audit_producto_update
AFTER UPDATE ON producto
FOR EACH ROW
BEGIN
    DECLARE usuario BIGINT DEFAULT 0;

    IF @current_user_id IS NOT NULL THEN
        SET usuario = @current_user_id;
    END IF;

    INSERT INTO auditoria (
        entidad,
        entidad_id,
        tipo_operacion,
        usuario_id,
        valores_anteriores,
        valores_nuevos
    ) VALUES (
        'producto',
        OLD.id,
        'UPDATE',
        usuario,
        JSON_OBJECT(
            'nombre', OLD.nombre,
            'categoria', OLD.categoria,
            'stock', OLD.stock,
            'precio', OLD.precio
        ),
        JSON_OBJECT(
            'nombre', NEW.nombre,
            'categoria', NEW.categoria,
            'stock', NEW.stock,
            'precio', NEW.precio
        )
    );
END$$


CREATE TRIGGER audit_producto_delete
AFTER DELETE ON producto
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (entidad, entidad_id, tipo_operacion, usuario_id, valores_anteriores)
    VALUES
    ('producto', OLD.id, 'DELETE', @current_user_id, 
        JSON_OBJECT('nombre', OLD.nombre, 'categoria', OLD.categoria, 'stock', OLD.stock, 'precio', OLD.precio));
END$$

DELIMITER ;
