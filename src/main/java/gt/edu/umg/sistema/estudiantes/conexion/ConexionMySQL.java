/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Angel Gomez
 */
public class ConexionMySQL {
    
    private static final String URL =
"jdbc:mysql://localhost:3306/sistema_estudiantes";
private static final String USER = "root";
private static final String PASSWORD = "199822";
public static Connection getConnection() {
    try {
        return DriverManager.getConnection(
            URL,
            USER,
            PASSWORD
        );
        } catch (SQLException e) {
            System.out.println("Error de conexión: "+ e.getMessage());
return null;
        }
    }    
}
