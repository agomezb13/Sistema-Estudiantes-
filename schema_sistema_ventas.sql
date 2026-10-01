-- ==========================================================
-- Base de Datos: sistema_ventas
-- Basado en Diagrama de Clases UML y Modelo Entidad-Relación
-- ==========================================================

CREATE DATABASE IF NOT EXISTS sistema_ventas CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE sistema_ventas;

-- 1. Tabla: cliente
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    numero_dpi VARCHAR(20),
    nit VARCHAR(20),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 2. Tabla: direccion_envio
CREATE TABLE IF NOT EXISTS direccion_envio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    calle VARCHAR(200) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(20),
    pais VARCHAR(100) NOT NULL,
    CONSTRAINT fk_direccion_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3. Tabla: vendedor
CREATE TABLE IF NOT EXISTS vendedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo_empleado VARCHAR(50) NOT NULL UNIQUE,
    departamento VARCHAR(100),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
) ENGINE=InnoDB;

-- 4. Tabla: categoria
CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
) ENGINE=InnoDB;

-- 5. Tabla: producto
CREATE TABLE IF NOT EXISTS producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    sku VARCHAR(50),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 6. Tabla: inventario
CREATE TABLE IF NOT EXISTS inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL UNIQUE,
    cantidad_disponible INT NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100),
    CONSTRAINT fk_inventario_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Tabla: carrito
CREATE TABLE IF NOT EXISTS carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrito_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. Tabla: elemento_carrito
CREATE TABLE IF NOT EXISTS elemento_carrito (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carrito_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_elem_carrito FOREIGN KEY (carrito_id) REFERENCES carrito(id) ON DELETE CASCADE,
    CONSTRAINT fk_elem_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 9. Tabla: pedido
CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    direccion_envio_id INT,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE RESTRICT,
    CONSTRAINT fk_pedido_direccion FOREIGN KEY (direccion_envio_id) REFERENCES direccion_envio(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 10. Tabla: detalle_pedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_det_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    CONSTRAINT fk_det_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 11. Tabla: pago
CREATE TABLE IF NOT EXISTS pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_pago_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 12. Tabla: factura
CREATE TABLE IF NOT EXISTS factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NULL,
    cliente_id INT NOT NULL,
    vendedor_id INT NOT NULL,
    numero VARCHAR(50) NOT NULL UNIQUE,
    fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(50) NOT NULL DEFAULT 'EMITIDA',
    CONSTRAINT fk_factura_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE SET NULL,
    CONSTRAINT fk_factura_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE RESTRICT,
    CONSTRAINT fk_factura_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedor(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 13. Tabla: detalle_factura
CREATE TABLE IF NOT EXISTS detalle_factura (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detfac_factura FOREIGN KEY (factura_id) REFERENCES factura(id) ON DELETE CASCADE,
    CONSTRAINT fk_detfac_producto FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Tabla de compatibilidad para gestión de estudiantes
CREATE TABLE IF NOT EXISTS estudiante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carnet VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(100),
    telefono VARCHAR(20)
) ENGINE=InnoDB;

-- ==========================================================
-- Datos de prueba iniciales (Seed Data)
-- ==========================================================

-- Categorías
INSERT INTO categoria (id, nombre, descripcion) VALUES
(1, 'Laptops y Cómputo', 'Equipos portátiles, componentes y accesorios'),
(2, 'Monitores y Pantallas', 'Monitores de alta resolución y accesorios'),
(3, 'Periféricos', 'Teclados, ratones, auriculares y accesorios de entrada')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Productos
INSERT INTO producto (id, categoria_id, nombre, descripcion, precio, stock, sku) VALUES
(1, 1, 'Laptop Dell Latitude 5420 Core i7 16GB', 'Laptop profesional para desarrollo y oficina', 8200.00, 15, 'LAP-DELL-5420'),
(2, 2, 'Monitor LG UltraWide 29 Pulgadas IPS', 'Monitor panorámico Full HD con FreeSync', 2450.00, 20, 'MON-LG-29'),
(3, 3, 'Teclado Mecánico Logitech G Pro X', 'Switches intercambiables RGB profesional', 850.00, 35, 'TEC-LOGI-GPRO'),
(4, 3, 'Mouse Inalámbrico Logitech MX Master 3S', 'Sensor óptico de alta precisión para productividad', 780.00, 40, 'MOU-LOGI-MX3S')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Inventario
INSERT INTO inventario (producto_id, cantidad_disponible, ubicacion) VALUES
(1, 15, 'Bodega Central - Estante A1'),
(2, 20, 'Bodega Central - Estante B2'),
(3, 35, 'Bodega Periféricos - Pasillo 1'),
(4, 40, 'Bodega Periféricos - Pasillo 1')
ON DUPLICATE KEY UPDATE cantidad_disponible=VALUES(cantidad_disponible);

-- Clientes
INSERT INTO cliente (id, nombre, correo, telefono, direccion, numero_dpi, nit, estado) VALUES
(1, 'Juan Pérez Gómez', 'juan.perez@correo.com', '5555-1234', '12 Calle 4-20 Zona 1, Guatemala', '1234567890101', '458923-1', 'ACTIVO'),
(2, 'María Fernanda López', 'maria.lopez@correo.com', '4444-5678', 'Avenida Las Américas 8-50 Zona 14, Guatemala', '2345678901201', '789123-K', 'ACTIVO'),
(3, 'Carlos Morales Ruíz', 'carlos.morales@correo.com', '3333-9012', 'Calzada Roosevelt 15-30 Zona 11, Guatemala', '3456789012301', '124578-8', 'ACTIVO')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Direcciones de Envío
INSERT INTO direccion_envio (id, cliente_id, calle, ciudad, codigo_postal, pais) VALUES
(1, 1, '12 Calle 4-20 Zona 1', 'Ciudad de Guatemala', '01001', 'Guatemala'),
(2, 2, 'Avenida Las Américas 8-50 Zona 14', 'Ciudad de Guatemala', '01014', 'Guatemala'),
(3, 3, 'Calzada Roosevelt 15-30 Zona 11', 'Ciudad de Guatemala', '01011', 'Guatemala')
ON DUPLICATE KEY UPDATE calle=VALUES(calle);

-- Vendedores
INSERT INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES
(1, 'Ana Sofía Rodríguez', 'VEND-001', 'Ventas Corporativas', '5123-4567', 'ana.rodriguez@empresa.com', 'ACTIVO'),
(2, 'Carlos Roberto Mendoza', 'VEND-002', 'Ventas Minoristas', '5234-5678', 'carlos.mendoza@empresa.com', 'ACTIVO'),
(3, 'Laura Isabel Castillo', 'VEND-003', 'Atención al Cliente y Mostrador', '5345-6789', 'laura.castillo@empresa.com', 'ACTIVO')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Pedidos iniciales
INSERT INTO pedido (id, cliente_id, direccion_envio_id, fecha, estado, total) VALUES
(1, 1, 1, NOW(), 'CONFIRMADO', 10650.00),
(2, 2, 2, NOW(), 'PENDIENTE', 1630.00)
ON DUPLICATE KEY UPDATE total=VALUES(total);

-- Detalles de Pedido
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, subtotal) VALUES
(1, 1, 1, 8200.00, 8200.00),
(1, 2, 1, 2450.00, 2450.00),
(2, 3, 1, 850.00, 850.00),
(2, 4, 1, 780.00, 780.00);

-- Factura inicial
INSERT INTO factura (id, pedido_id, cliente_id, vendedor_id, numero, fecha_emision, subtotal, impuesto, total, estado) VALUES
(1, 1, 1, 1, 'FAC-2024-001', NOW(), 10650.00, 1278.00, 11928.00, 'EMITIDA')
ON DUPLICATE KEY UPDATE numero=VALUES(numero);

-- Detalles de Factura
INSERT INTO detalle_factura (factura_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 8200.00, 8200.00),
(1, 2, 1, 2450.00, 2450.00);

-- Pago inicial
INSERT INTO pago (id, pedido_id, monto, metodo, estado) VALUES
(1, 1, 10650.00, 'TRANSFERENCIA', 'PAGADO')
ON DUPLICATE KEY UPDATE monto=VALUES(monto);
