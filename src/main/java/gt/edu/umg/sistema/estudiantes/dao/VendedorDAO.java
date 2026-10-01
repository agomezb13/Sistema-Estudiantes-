package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.util.List;

public interface VendedorDAO {
    void guardar(Vendedor vendedor);
    List<Vendedor> listar();
    Vendedor buscarPorId(int id);
    List<Vendedor> buscar(String nombre, String correo);
    void actualizar(Vendedor vendedor);
    void eliminar(int id);
}
