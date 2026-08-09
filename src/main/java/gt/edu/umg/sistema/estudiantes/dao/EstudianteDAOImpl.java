/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maorozco
 */
public class EstudianteDAOImpl implements EstudianteDAO {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    
    
    @Override
    public void guardar(Estudiante estutidante) {
      
        estudiantes.add(estutidante);
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
