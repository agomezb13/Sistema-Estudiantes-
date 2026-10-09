package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Producto producto) {
        boolean esNuevo = (producto.getId() <= 0) || (buscarPorId(producto.getId()) == null);

        if (producto.getId() <= 0) {
            producto.setId(db.siguienteIdProducto());
        }

        if (esNuevo) {
            db.getProductos().removeIf(p -> p.getId() == producto.getId());
            db.getProductos().add(producto);

            Connection cn = ConexionMySQL.getConnection();
            if (cn != null) {
                int catId = producto.getCategoriaId() <= 0 ? 1 : producto.getCategoriaId();
                try (Statement st = cn.createStatement()) {
                    st.executeUpdate("INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (" + catId + ", 'General', 'Categoria General')");
                } catch (SQLException ignored) {
                }

                String sql = "INSERT INTO producto (id, categoria_id, nombre, descripcion, precio, stock, sku) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE categoria_id=VALUES(categoria_id), nombre=VALUES(nombre), "
                        + "descripcion=VALUES(descripcion), precio=VALUES(precio), stock=VALUES(stock), sku=VALUES(sku)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, producto.getId());
                    ps.setInt(2, catId);
                    ps.setString(3, producto.getNombre());
                    ps.setString(4, producto.getDescripcion() == null ? "" : producto.getDescripcion());
                    ps.setDouble(5, producto.getPrecio());
                    ps.setInt(6, producto.getExistencias());
                    ps.setString(7, "PROD-" + producto.getId());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Aviso al persistir producto en BD: " + e.getMessage());
                } finally {
                    try {
                        cn.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        } else {
            actualizar(producto);
        }
    }

    @Override
    public List<Producto> listar() {
        if (db.getProductos().isEmpty()) {
            db.cargarDatosDesdeBD();
        }
        return new ArrayList<>(db.getProductos());
    }

    @Override
    public Producto buscarPorId(int id) {
        for (Producto p : listar()) {
            if (p.getId() == id) {
                return p;
            }
        }
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, categoria_id, nombre, descripcion, precio, stock FROM producto WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Producto prod = new Producto(
                                rs.getInt("id"),
                                rs.getInt("categoria_id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("precio"),
                                rs.getInt("stock")
                        );
                        db.getProductos().add(prod);
                        return prod;
                    }
                }
            } catch (SQLException ignored) {
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscar(String nombre, Integer categoriaId) {
        List<Producto> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        for (Producto p : listar()) {
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
        Producto actual = null;
        for (Producto p : db.getProductos()) {
            if (p.getId() == producto.getId()) {
                actual = p;
                break;
            }
        }
        if (actual == null) {
            db.getProductos().add(producto);
        } else {
            actual.setCategoriaId(producto.getCategoriaId());
            actual.setNombre(producto.getNombre());
            actual.setDescripcion(producto.getDescripcion());
            actual.setPrecio(producto.getPrecio());
            actual.setExistencias(producto.getExistencias());
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            int catId = producto.getCategoriaId() <= 0 ? 1 : producto.getCategoriaId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (" + catId + ", 'General', 'Categoria General')");
            } catch (SQLException ignored) {
            }

            String sql = "UPDATE producto SET categoria_id = ?, nombre = ?, descripcion = ?, precio = ?, stock = ? WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, catId);
                ps.setString(2, producto.getNombre());
                ps.setString(3, producto.getDescripcion() == null ? "" : producto.getDescripcion());
                ps.setDouble(4, producto.getPrecio());
                ps.setInt(5, producto.getExistencias());
                ps.setInt(6, producto.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al actualizar producto en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
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
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
