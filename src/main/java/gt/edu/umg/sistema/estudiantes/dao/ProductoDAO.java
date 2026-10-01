package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.util.List;

public interface ProductoDAO {
    void guardar(Producto producto);
    List<Producto> listar();
    Producto buscarPorId(int id);
    List<Producto> buscar(String nombre, Integer categoriaId);
    void actualizar(Producto producto);
    void eliminar(int id);
}
