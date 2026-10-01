package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.ProductoDAO;
import gt.edu.umg.sistema.estudiantes.dao.ProductoDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.util.List;

public class InventarioController {

    private final ProductoDAO productoDAO;

    public InventarioController() {
        this.productoDAO = new ProductoDAOImpl();
    }

    public InventarioController(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }

    public List<Producto> buscar() {
        return productoDAO.listar();
    }

    public List<Producto> filtrar(Integer categoriaId) {
        return productoDAO.buscar("", categoriaId);
    }

    public Producto verDetalle(int productoId) {
        return productoDAO.buscarPorId(productoId);
    }

    public void actualizarStock(int productoId, int nuevoStock) {
        Producto p = productoDAO.buscarPorId(productoId);
        if (p != null) {
            p.setExistencias(nuevoStock);
            productoDAO.actualizar(p);
        }
    }
}
