package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.util.List;

public interface CategoriaDAO {
    void guardar(Categoria categoria);
    List<Categoria> listar();
    Categoria buscarPorId(int id);
    List<Categoria> buscar(String nombre);
    void actualizar(Categoria categoria);
    void eliminar(int id);
}
