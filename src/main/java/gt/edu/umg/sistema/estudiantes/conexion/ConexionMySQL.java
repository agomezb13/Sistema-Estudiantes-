package gt.edu.umg.sistema.estudiantes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionMySQL {

    private static final String HOST = "159.54.148.6";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "sistema_ventas";
    private static final String USER = "root";
    private static final String PASSWORD = "4147";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static boolean inicializado = false;

    public static Connection getConnection() {
        if (!inicializado) {
            inicializado = true;
            inicializarBaseDatos();
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL no encontrado: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Error de conexion a MySQL: " + e.getMessage());
            return null;
        }
    }

    public static boolean probarConexion() {
        try (Connection cn = getConnection()) {
            return cn != null && !cn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexion a MySQL...");
        inicializarBaseDatos();
        if (probarConexion()) {
            System.out.println("EXITO: Conexion establecida con MySQL en " + HOST + ":" + PUERTO + "/" + BASE_DATOS);
        } else {
            System.out.println("AVISO: No se pudo conectar con las credenciales actuales.");
        }
    }

    public static void inicializarBaseDatos() {
        inicializado = true;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String urlServer = "jdbc:mysql://" + HOST + ":" + PUERTO
                    + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            try (Connection cnServer = DriverManager.getConnection(urlServer, USER, PASSWORD)) {
                try (Statement stmt = cnServer.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + BASE_DATOS
                            + " CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci");
                }
            } catch (SQLException ignored) {
            }

            try (Connection cnBD = DriverManager.getConnection(URL, USER, PASSWORD)) {
                try (Statement stmt = cnBD.createStatement()) {
                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS cliente ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "nombre VARCHAR(100) NOT NULL, "
                            + "correo VARCHAR(100) NOT NULL, "
                            + "telefono VARCHAR(20), "
                            + "direccion VARCHAR(255), "
                            + "numero_dpi VARCHAR(20), "
                            + "nit VARCHAR(20), "
                            + "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO') ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS direccion_envio ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "cliente_id INT NOT NULL, "
                            + "calle VARCHAR(200) NOT NULL, "
                            + "ciudad VARCHAR(100) NOT NULL, "
                            + "codigo_postal VARCHAR(20), "
                            + "pais VARCHAR(100) NOT NULL) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS vendedor ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "nombre VARCHAR(100) NOT NULL, "
                            + "codigo_empleado VARCHAR(50) NOT NULL UNIQUE, "
                            + "departamento VARCHAR(100), "
                            + "telefono VARCHAR(20), "
                            + "correo VARCHAR(100), "
                            + "estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO') ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS categoria ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "nombre VARCHAR(100) NOT NULL, "
                            + "descripcion VARCHAR(255)) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS producto ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "categoria_id INT NOT NULL, "
                            + "nombre VARCHAR(150) NOT NULL, "
                            + "descripcion TEXT, "
                            + "precio DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                            + "stock INT NOT NULL DEFAULT 0, "
                            + "sku VARCHAR(50)) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pedido ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "cliente_id INT NOT NULL, "
                            + "direccion_envio_id INT, "
                            + "fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                            + "estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE', "
                            + "total DECIMAL(10,2) NOT NULL DEFAULT 0.00) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS detalle_pedido ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "pedido_id INT NOT NULL, "
                            + "producto_id INT NOT NULL, "
                            + "cantidad INT NOT NULL, "
                            + "precio DECIMAL(10,2) NOT NULL, "
                            + "subtotal DECIMAL(10,2) NOT NULL) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS pago ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "pedido_id INT NOT NULL, "
                            + "monto DECIMAL(10,2) NOT NULL, "
                            + "metodo VARCHAR(50) NOT NULL, "
                            + "estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE') ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS factura ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "pedido_id INT NULL, "
                            + "cliente_id INT NOT NULL, "
                            + "vendedor_id INT NOT NULL, "
                            + "numero VARCHAR(50) NOT NULL UNIQUE, "
                            + "fecha_emision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                            + "subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                            + "impuesto DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                            + "total DECIMAL(10,2) NOT NULL DEFAULT 0.00, "
                            + "estado VARCHAR(50) NOT NULL DEFAULT 'EMITIDA') ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS detalle_factura ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "factura_id INT NOT NULL, "
                            + "producto_id INT NOT NULL, "
                            + "cantidad INT NOT NULL, "
                            + "precio_unitario DECIMAL(10,2) NOT NULL, "
                            + "subtotal DECIMAL(10,2) NOT NULL) ENGINE=InnoDB");

                    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS estudiante ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY, "
                            + "carnet VARCHAR(20) NOT NULL UNIQUE, "
                            + "nombres VARCHAR(100) NOT NULL, "
                            + "apellidos VARCHAR(100) NOT NULL, "
                            + "email VARCHAR(100), "
                            + "correo VARCHAR(100), "
                            + "telefono VARCHAR(20)) ENGINE=InnoDB");

                    stmt.executeUpdate("INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (1, 'General', 'Categoria General')");
                    stmt.executeUpdate("INSERT IGNORE INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES (1, 'Administrador', 'VEND-001', 'Ventas', '55555555', 'admin@tienda.com', 'ACTIVO')");
                }
            }
        } catch (Exception e) {
            System.out.println("Aviso: MySQL no disponible (" + e.getMessage() + ")");
        }
    }

    public static String getHost() {
        return HOST;
    }

    public static String getPuerto() {
        return PUERTO;
    }

    public static String getBaseDatos() {
        return BASE_DATOS;
    }

    public static String getUser() {
        return USER;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
