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

-- Stock por Bodega y Producto (para transferencias e inventario real por ubicación)
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
    INSERT INTO auditoria (entidad, entidad_id, tipo_operacion, usuario_id, valores_anteriores, valores_nuevos)
    VALUES 
    ('producto', OLD.id, 'UPDATE', @current_user_id, 
        JSON_OBJECT('nombre', OLD.nombre, 'categoria', OLD.categoria, 'stock', OLD.stock, 'precio', OLD.precio),
        JSON_OBJECT('nombre', NEW.nombre, 'categoria', NEW.categoria, 'stock', NEW.stock, 'precio', NEW.precio));
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

-- Procedimientos
-- Productos con stock bajo en cualquier bodega
DROP PROCEDURE IF EXISTS productos_stock_bajo;
DELIMITER $$
CREATE PROCEDURE productos_stock_bajo()
BEGIN
    SELECT p.id, p.nombre, b.nombre AS bodega, bp.stock
    FROM producto p
    JOIN bodega_producto bp ON p.id = bp.producto_id
    JOIN bodega b ON bp.bodega_id = b.id
    WHERE bp.stock < 10;
END$$
DELIMITER ;

-- ====================================================
-- PROCEDURE: Movimientos en rango de fechas
-- ====================================================
DROP PROCEDURE IF EXISTS movimientos_por_fecha;
DELIMITER $$
CREATE PROCEDURE movimientos_por_fecha(IN fecha_ini DATETIME, IN fecha_fin DATETIME)
BEGIN
    SELECT 
        m.id,
        m.fecha,
        m.tipo,
        m.usuario_id,
        m.bodega_origen_id,
        m.bodega_destino_id,
        m.comentario
    FROM movimiento m
    WHERE m.fecha BETWEEN fecha_ini AND fecha_fin
    ORDER BY m.fecha DESC;
END$$
DELIMITER ;



-- ====================================================
-- PROCEDURE: Auditoría filtrada por usuario o tipo
-- ====================================================
DROP PROCEDURE IF EXISTS auditoria_filtro;
DELIMITER $$
CREATE PROCEDURE auditoria_filtro(
    IN usuario INT,
    IN tipo_op VARCHAR(10)
)
BEGIN
    SELECT 
        a.id,
        a.entidad,
        a.entidad_id,
        a.tipo_operacion,
        a.usuario_id,
        a.fecha,
        a.valores_anteriores,
        a.valores_nuevos
    FROM auditoria a
    WHERE (a.usuario_id = usuario OR usuario IS NULL)
      AND (a.tipo_operacion = tipo_op OR tipo_op IS NULL)
    ORDER BY a.fecha DESC;
END$$
DELIMITER ;



-- ====================================================
-- VIEW: Resumen de stock total por bodega
-- ====================================================
DROP VIEW IF EXISTS resumen_stock_bodega;
CREATE VIEW resumen_stock_bodega AS
SELECT
    b.id AS bodega_id,
    b.nombre AS bodega,
    p.id AS producto_id,
    p.nombre AS producto,
    bp.stock
FROM bodega_producto bp
JOIN bodega b ON bp.bodega_id = b.id
JOIN producto p ON bp.producto_id = p.id
ORDER BY b.nombre, p.nombre;
