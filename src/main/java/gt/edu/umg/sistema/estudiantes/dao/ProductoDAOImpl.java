package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "INSERT INTO producto (id, categoria_id, nombre, descripcion, precio, stock, sku) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE categoria_id=VALUES(categoria_id), nombre=VALUES(nombre), "
                    + "descripcion=VALUES(descripcion), precio=VALUES(precio), stock=VALUES(stock), sku=VALUES(sku)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, producto.getId());
                ps.setInt(2, producto.getCategoriaId() <= 0 ? 1 : producto.getCategoriaId());
                ps.setString(3, producto.getNombre());
                ps.setString(4, producto.getDescripcion() == null ? "" : producto.getDescripcion());
                ps.setDouble(5, producto.getPrecio());
                ps.setInt(6, producto.getExistencias());
                ps.setString(7, "PROD-" + producto.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al persistir producto en BD: " + e.getMessage());
            }
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
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM producto WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar producto en BD: " + e.getMessage());
            }
        }
    }
}
