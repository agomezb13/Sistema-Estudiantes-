package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.List;

public interface EstudianteDAO {
    void guardar(Estudiante estudiante);
    List<Estudiante> listar();
    void actualizar(Estudiante estudiante);
    void eliminar(int id);
}
