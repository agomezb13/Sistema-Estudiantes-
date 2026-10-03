/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;
import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author maorozco
 */
public class EstudianteDAOImpl implements EstudianteDAO {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    
    
    @Override
    public void guardar(Estudiante estudiante) {
        estudiantes.add(estudiante);
        Connection conexion = ConexionMySQL.getConnection();
        if (conexion != null) {
            String sql = "INSERT INTO estudiante (id, nombres, apellidos, email, carnet) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, estudiante.getId());
                ps.setString(2, estudiante.getNombres());
                ps.setString(3, estudiante.getApellidos());
                ps.setString(4, estudiante.getEmail());
                ps.setString(5, estudiante.getCarnet());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al guardar estudiante en BD: " + e.getMessage());
            } finally {
                try {
                    conexion.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Estudiante> listar() {
        return estudiantes;
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminar(int id) {
        estudiantes.removeIf(estudiante -> estudiante.getId() == id);
    }
    
}
