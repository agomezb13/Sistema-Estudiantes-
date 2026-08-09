/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.EstudianteDAO;
import gt.edu.umg.sistema.estudiantes.dao.EstudianteDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.List;

/**
 *
 * @author maorozco
 */
public class EstudianteController {
    
    EstudianteDAOImpl dao;
    
   public EstudianteController(){
       dao = new EstudianteDAOImpl(); 
   }
    
    public void Guardar(Estudiante estudiante){
                dao.guardar(estudiante);
    }
    
    public List<Estudiante> GetEstudiantes(){
                return dao.listar();
    }
}
