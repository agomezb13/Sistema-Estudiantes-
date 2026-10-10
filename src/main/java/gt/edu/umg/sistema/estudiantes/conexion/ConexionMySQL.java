package gt.edu.umg.sistema.estudiantes.conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Administrador de conexion JDBC con MySQL.
 * 
 * Caracteristicas principales:
 * 1. Lee la configuracion (host, puerto, usuario, contrasena) desde el archivo 'db.properties'.
 *    Por defecto se conecta al servidor en la nube Oracle Always Free.
 * 2. Si las tablas no existen al iniciar la aplicacion, las crea automaticamente
 *    mediante sentencias DDL (CREATE TABLE IF NOT EXISTS).
 * 3. Si las tablas estan vacias, inserta datos semilla de prueba (administrador, categorias...).
 */
public class ConexionMySQL {

    private static String host = "localhost";
    private static String puerto = "3306";
    private static String baseDatos = "sistema_ventas";
    private static String user = "root";
    private static String password = "4147";
    private static boolean inicializado = false;

    private static final String ARCHIVO_PROPIEDADES = "db.properties";

    static {
        cargarConfiguracion();
    }

    public static void cargarConfiguracion() {
        Properties props = new Properties();
        File f = new File(ARCHIVO_PROPIEDADES);
        if (f.exists()) {
            try (FileInputStream in = new FileInputStream(f)) {
                props.load(in);
            } catch (IOException e) {
                System.out.println("Aviso al leer " + ARCHIVO_PROPIEDADES + ": " + e.getMessage());
            }
        } else {
            try (var stream = ConexionMySQL.class.getClassLoader().getResourceAsStream(ARCHIVO_PROPIEDADES)) {
                if (stream != null) {
                    props.load(stream);
                }
            } catch (IOException ignored) {
            }
        }

        if (!props.isEmpty()) {
            host = props.getProperty("db.host", host);
            puerto = props.getProperty("db.port", puerto);
            baseDatos = props.getProperty("db.name", baseDatos);
            user = props.getProperty("db.user", user);
            password = props.getProperty("db.password", password);
        }
    }

    public static void guardarConfiguracion(String nuevoHost, String nuevoPuerto, String nuevaBD, String nuevoUser, String nuevoPass) {
        host = nuevoHost;
        puerto = nuevoPuerto;
        baseDatos = nuevaBD;
        user = nuevoUser;
        password = nuevoPass;

        Properties props = new Properties();
        props.setProperty("db.host", host);
        props.setProperty("db.port", puerto);
        props.setProperty("db.name", baseDatos);
        props.setProperty("db.user", user);
        props.setProperty("db.password", password);

        try (FileOutputStream out = new FileOutputStream(ARCHIVO_PROPIEDADES)) {
            props.store(out, "Configuracion conexion MySQL Workbench");
        } catch (IOException e) {
            System.out.println("Aviso al guardar " + ARCHIVO_PROPIEDADES + ": " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        if (!inicializado) {
            inicializado = true;
            inicializarBaseDatos();
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            try {
                return DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                if (!password.isEmpty()) {
                    try {
                        return DriverManager.getConnection(url, user, "");
                    } catch (SQLException ex2) {
                        // Continuar si no se pudo conectar
                    }
                }
                return null;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Driver MySQL no encontrado: " + e.getMessage());
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
            System.out.println("EXITO: Conexion establecida con MySQL en " + host + ":" + puerto + "/" + baseDatos);
        } else {
            System.out.println("AVISO: No se pudo conectar con las credenciales actuales.");
        }
    }

    public static void inicializarBaseDatos() {
        inicializado = true;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String urlServer = "jdbc:mysql://" + host + ":" + puerto
                    + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            Connection cnServer = null;
            try {
                cnServer = DriverManager.getConnection(urlServer, user, password);
            } catch (SQLException e) {
                try {
                    cnServer = DriverManager.getConnection(urlServer, user, "");
                } catch (SQLException ignored) {
                }
            }

            if (cnServer != null) {
                try (Statement stmt = cnServer.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + baseDatos
                            + " CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci");
                }
                cnServer.close();
            }

            String urlBD = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            Connection cnBD = null;
            try {
                cnBD = DriverManager.getConnection(urlBD, user, password);
            } catch (SQLException e) {
                try {
                    cnBD = DriverManager.getConnection(urlBD, user, "");
                } catch (SQLException ignored) {
                }
            }
            if (cnBD != null) {
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
                cnBD.close();
            }
        } catch (Exception e) {
            System.out.println("Aviso: MySQL no disponible (" + e.getMessage() + ")");
        }
    }

    public static void configurar(String nuevoHost, String nuevoPuerto, String nuevaBD, String nuevoUser, String nuevoPass) {
        host = nuevoHost;
        puerto = nuevoPuerto;
        baseDatos = nuevaBD;
        user = nuevoUser;
        password = nuevoPass;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String nuevoHost) {
        host = nuevoHost;
    }

    public static String getPuerto() {
        return puerto;
    }

    public static void setPuerto(String nuevoPuerto) {
        puerto = nuevoPuerto;
    }

    public static String getBaseDatos() {
        return baseDatos;
    }

    public static void setBaseDatos(String nuevaBD) {
        baseDatos = nuevaBD;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String nuevoUser) {
        user = nuevoUser;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String nuevoPassword) {
        password = nuevoPassword;
    }
}
