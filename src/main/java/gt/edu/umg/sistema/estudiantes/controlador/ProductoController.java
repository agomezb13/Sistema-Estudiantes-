package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.ProductoDAO;
import gt.edu.umg.sistema.estudiantes.dao.ProductoDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.util.List;

public class ProductoController {

    private final ProductoDAO dao;

    public ProductoController() {
        this.dao = new ProductoDAOImpl();
    }

    public ProductoController(ProductoDAO dao) {
        this.dao = dao;
    }

    public void guardar(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (producto.getPrecio() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        dao.guardar(producto);
    }

    public List<Producto> listar() {
        return dao.listar();
    }

    public Producto buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Producto> buscar(String nombre, Integer categoriaId) {
        return dao.buscar(nombre, categoriaId);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }

    public boolean descontarStock(int productoId, int cantidad) {
        return dao.descontarStock(productoId, cantidad);
    }

    public void reponerStock(int productoId, int cantidad) {
        dao.reponerStock(productoId, cantidad);
    }
}
