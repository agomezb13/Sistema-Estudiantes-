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

-- Tabla para gestion de estudiantes
CREATE TABLE IF NOT EXISTS estudiante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carnet VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    correo VARCHAR(100),
    telefono VARCHAR(20)
) ENGINE=InnoDB;
