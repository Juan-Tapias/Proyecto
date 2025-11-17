-- 1. AUDITORÍA COMPLETA CON DETALLES DE USUARIO
SELECT 
    a.id,
    a.entidad,
    a.entidad_id,
    a.tipo_operacion,
    u.username AS usuario,
    u.nombre AS nombre_usuario,
    a.fecha,
    a.valores_anteriores,
    a.valores_nuevos
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
ORDER BY a.fecha DESC;

-- 2. AUDITORÍA POR USUARIO ESPECÍFICO
SELECT 
    a.id,
    a.entidad,
    a.entidad_id,
    a.tipo_operacion,
    u.username,
    a.fecha,
    JSON_EXTRACT(a.valores_anteriores, '$.nombre') AS nombre_anterior,
    JSON_EXTRACT(a.valores_nuevos, '$.nombre') AS nombre_nuevo,
    JSON_EXTRACT(a.valores_anteriores, '$.precio') AS precio_anterior,
    JSON_EXTRACT(a.valores_nuevos, '$.precio') AS precio_nuevo,
    JSON_EXTRACT(a.valores_anteriores, '$.stock') AS stock_anterior,
    JSON_EXTRACT(a.valores_nuevos, '$.stock') AS stock_nuevo
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
WHERE u.username = 'jlopez'  
ORDER BY a.fecha DESC;

-- 3. AUDITORÍA POR TIPO DE OPERACIÓN
SELECT 
    a.tipo_operacion,
    COUNT(*) as total_operaciones,
    u.username,
    u.nombre,
    MIN(a.fecha) as primera_operacion,
    MAX(a.fecha) as ultima_operacion
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
WHERE a.tipo_operacion = 'UPDATE'  
GROUP BY a.tipo_operacion, u.id, u.username, u.nombre
ORDER BY total_operaciones DESC;

-- 4. REPORTE DE ACTIVIDAD POR USUARIO (RESUMEN)
SELECT 
    u.username,
    u.nombre,
    u.rol,
    COUNT(a.id) as total_operaciones,
    SUM(CASE WHEN a.tipo_operacion = 'INSERT' THEN 1 ELSE 0 END) as inserciones,
    SUM(CASE WHEN a.tipo_operacion = 'UPDATE' THEN 1 ELSE 0 END) as actualizaciones,
    SUM(CASE WHEN a.tipo_operacion = 'DELETE' THEN 1 ELSE 0 END) as eliminaciones,
    MIN(a.fecha) as primera_operacion,
    MAX(a.fecha) as ultima_operacion
FROM usuario u
LEFT JOIN auditoria a ON u.id = a.usuario_id
GROUP BY u.id, u.username, u.nombre, u.rol
ORDER BY total_operaciones DESC;

-- 5. REPORTE DE CAMBIOS DE PRECIO
SELECT 
    a.fecha,
    u.username,
    p.nombre AS producto_actual,
    JSON_EXTRACT(a.valores_anteriores, '$.nombre') AS nombre_anterior,
    CAST(JSON_EXTRACT(a.valores_anteriores, '$.precio') AS DECIMAL(12,2)) AS precio_anterior,
    CAST(JSON_EXTRACT(a.valores_nuevos, '$.precio') AS DECIMAL(12,2)) AS precio_nuevo,
    ROUND(((CAST(JSON_EXTRACT(a.valores_nuevos, '$.precio') AS DECIMAL(12,2)) - 
           CAST(JSON_EXTRACT(a.valores_anteriores, '$.precio') AS DECIMAL(12,2))) / 
           CAST(JSON_EXTRACT(a.valores_anteriores, '$.precio') AS DECIMAL(12,2)) * 100), 2) AS porcentaje_cambio
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
LEFT JOIN producto p ON a.entidad_id = p.id
WHERE a.entidad = 'producto'
AND a.tipo_operacion = 'UPDATE'
AND CAST(JSON_EXTRACT(a.valores_anteriores, '$.precio') AS DECIMAL(12,2)) != 
    CAST(JSON_EXTRACT(a.valores_nuevos, '$.precio') AS DECIMAL(12,2))
ORDER BY ABS(porcentaje_cambio) DESC;

-- 6. AUDITORÍA POR RANGO DE FECHAS
SELECT 
    a.entidad,
    a.tipo_operacion,
    u.username,
    COUNT(*) as total_operaciones,
    DATE(a.fecha) as fecha_dia
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
WHERE a.fecha BETWEEN '2024-01-01 00:00:00' AND '2024-12-31 23:59:59'  -- Cambiar fechas
GROUP BY a.entidad, a.tipo_operacion, u.username, DATE(a.fecha)
ORDER BY fecha_dia DESC, total_operaciones DESC;

-- 7. REPORTE DE STOCK MODIFICADO
SELECT 
    a.fecha,
    u.username,
    p.nombre AS producto,
    p.categoria,
    CAST(JSON_EXTRACT(a.valores_anteriores, '$.stock') AS UNSIGNED) AS stock_anterior,
    CAST(JSON_EXTRACT(a.valores_nuevos, '$.stock') AS UNSIGNED) AS stock_nuevo,
    CAST(JSON_EXTRACT(a.valores_nuevos, '$.stock') AS UNSIGNED) - 
    CAST(JSON_EXTRACT(a.valores_anteriores, '$.stock') AS UNSIGNED) AS cambio_stock,
    CASE 
        WHEN CAST(JSON_EXTRACT(a.valores_nuevos, '$.stock') AS UNSIGNED) > 
             CAST(JSON_EXTRACT(a.valores_anteriores, '$.stock') AS UNSIGNED) THEN 'INCREMENTO'
        WHEN CAST(JSON_EXTRACT(a.valores_nuevos, '$.stock') AS UNSIGNED) < 
             CAST(JSON_EXTRACT(a.valores_anteriores, '$.stock') AS UNSIGNED) THEN 'DECREMENTO'
        ELSE 'SIN CAMBIO'
    END AS tipo_cambio
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
LEFT JOIN producto p ON a.entidad_id = p.id
WHERE a.entidad = 'producto'
AND CAST(JSON_EXTRACT(a.valores_anteriores, '$.stock') AS UNSIGNED) != 
    CAST(JSON_EXTRACT(a.valores_nuevos, '$.stock') AS UNSIGNED)
ORDER BY ABS(cambio_stock) DESC;

-- 8. TOP 10 USUARIOS MÁS ACTIVOS
SELECT 
    u.username,
    u.nombre,
    u.rol,
    COUNT(a.id) as total_operaciones,
    COUNT(DISTINCT DATE(a.fecha)) as dias_activos,
    ROUND(COUNT(a.id) / COUNT(DISTINCT DATE(a.fecha)), 2) as operaciones_por_dia
FROM usuario u
INNER JOIN auditoria a ON u.id = a.usuario_id
GROUP BY u.id, u.username, u.nombre, u.rol
ORDER BY total_operaciones DESC
LIMIT 10;

-- 9. AUDITORÍA CONSOLIDADA POR ENTIDAD Y OPERACIÓN
SELECT 
    a.entidad,
    a.tipo_operacion,
    COUNT(*) as total_registros,
    COUNT(DISTINCT a.usuario_id) as usuarios_involucrados,
    MIN(a.fecha) as primera_operacion,
    MAX(a.fecha) as ultima_operacion,
    ROUND(AVG(JSON_LENGTH(a.valores_anteriores)), 2) as avg_campos_anteriores,
    ROUND(AVG(JSON_LENGTH(a.valores_nuevos)), 2) as avg_campos_nuevos
FROM auditoria a
GROUP BY a.entidad, a.tipo_operacion
ORDER BY a.entidad, total_registros DESC;


-- 10. REPORTE DE ACTIVIDAD POR HORA DEL DÍA
SELECT 
    HOUR(a.fecha) as hora_dia,
    COUNT(*) as total_operaciones,
    COUNT(DISTINCT a.usuario_id) as usuarios_activos,
    GROUP_CONCAT(DISTINCT u.username) as usuarios
FROM auditoria a
INNER JOIN usuario u ON a.usuario_id = u.id
GROUP BY HOUR(a.fecha)
ORDER BY hora_dia;