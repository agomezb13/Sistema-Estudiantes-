package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.EstudianteDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.List;

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

    public void Eliminar(int id) {
        dao.eliminar(id);
    }
}
