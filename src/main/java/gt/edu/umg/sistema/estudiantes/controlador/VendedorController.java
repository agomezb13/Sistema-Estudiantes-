package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.VendedorDAO;
import gt.edu.umg.sistema.estudiantes.dao.VendedorDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.util.List;

public class VendedorController {

    private final VendedorDAO dao;

    public VendedorController() {
        this.dao = new VendedorDAOImpl();
    }

    public VendedorController(VendedorDAO dao) {
        this.dao = dao;
    }

    public void guardar(Vendedor vendedor) {
        if (vendedor.getNombre() == null || vendedor.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del vendedor es obligatorio.");
        }
        dao.guardar(vendedor);
    }

    public List<Vendedor> listar() {
        return dao.listar();
    }

    public Vendedor buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Vendedor> buscar(String nombre, String correo) {
        return dao.buscar(nombre, correo);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
