package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.ClienteDAO;
import gt.edu.umg.sistema.estudiantes.dao.ClienteDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.util.List;

public class ClienteController {

    private final ClienteDAO dao;

    public ClienteController() {
        this.dao = new ClienteDAOImpl();
    }

    public ClienteController(ClienteDAO dao) {
        this.dao = dao;
    }

    public void guardar(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        dao.guardar(cliente);
    }

    public List<Cliente> listar() {
        return dao.listar();
    }

    public Cliente buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Cliente> buscar(String nit, String nombre) {
        return dao.buscar(nit, nombre);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
