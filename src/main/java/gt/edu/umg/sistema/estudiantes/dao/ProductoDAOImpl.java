package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Producto producto) {
        if (producto.getId() == 0) {
            producto.setId(db.siguienteIdProducto());
            db.getProductos().add(producto);
        } else {
            actualizar(producto);
        }
    }

    @Override
    public List<Producto> listar() {
        return new ArrayList<>(db.getProductos());
    }

    @Override
    public Producto buscarPorId(int id) {
        for (Producto p : db.getProductos()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscar(String nombre, Integer categoriaId) {
        List<Producto> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        for (Producto p : db.getProductos()) {
            boolean coincideNombre = nLower.isEmpty() || (p.getNombre() != null && p.getNombre().toLowerCase().contains(nLower));
            boolean coincideCat = categoriaId == null || p.getCategoriaId() == categoriaId;
            if (coincideNombre && coincideCat) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Producto producto) {
        Producto actual = buscarPorId(producto.getId());
        if (actual == null) {
            db.getProductos().add(producto);
            return;
        }
        actual.setCategoriaId(producto.getCategoriaId());
        actual.setNombre(producto.getNombre());
        actual.setDescripcion(producto.getDescripcion());
        actual.setPrecio(producto.getPrecio());
        actual.setExistencias(producto.getExistencias());
    }

    @Override
    public void eliminar(int id) {
        db.getProductos().removeIf(p -> p.getId() == id);
    }
}
