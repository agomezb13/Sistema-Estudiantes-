package gt.edu.umg.sistema.estudiantes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private static String host = "localhost";
    private static String puerto = "3306";
    private static String baseDatos = "sistema_ventas";
    private static String user = "root";
    private static String password = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Aviso: No se pudo conectar a MySQL (" + e.getMessage() + ")");
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

    public static String getPuerto() {
        return puerto;
    }

    public static String getBaseDatos() {
        return baseDatos;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
