package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para la entidad Cliente.
 * 
 * Define las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * abstrayendo la tecnologia de persistencia del resto de la aplicacion.
 */
public interface ClienteDAO {
    void guardar(Cliente cliente);
    List<Cliente> listar();
    Cliente buscarPorId(int id);
    List<Cliente> buscar(String nit, String nombre);
    void actualizar(Cliente cliente);
    void eliminar(int id);
}
