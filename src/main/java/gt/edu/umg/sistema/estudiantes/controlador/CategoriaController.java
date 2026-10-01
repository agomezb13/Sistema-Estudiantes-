package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.CategoriaDAO;
import gt.edu.umg.sistema.estudiantes.dao.CategoriaDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.util.List;

public class CategoriaController {

    private final CategoriaDAO dao;

    public CategoriaController() {
        this.dao = new CategoriaDAOImpl();
    }

    public CategoriaController(CategoriaDAO dao) {
        this.dao = dao;
    }

    public void guardar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        dao.guardar(categoria);
    }

    public List<Categoria> listar() {
        return dao.listar();
    }

    public Categoria buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Categoria> buscar(String nombre) {
        return dao.buscar(nombre);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
