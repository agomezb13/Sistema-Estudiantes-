package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public void guardar(Producto producto) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int catId = producto.getCategoriaId() <= 0 ? 1 : producto.getCategoriaId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO categoria (id, nombre, descripcion) VALUES (" + catId + ", 'General', 'General')");
            } catch (SQLException ignored) {
            }

            if (producto.getId() > 0) {
                if (buscarPorId(producto.getId()) != null) {
                    actualizar(producto);
                    return;
                }
                String sql = "INSERT INTO producto (id, categoria_id, nombre, descripcion, precio, stock, sku) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, producto.getId());
                    ps.setInt(2, catId);
                    ps.setString(3, producto.getNombre());
                    ps.setString(4, producto.getDescripcion() == null ? "" : producto.getDescripcion());
                    ps.setDouble(5, producto.getPrecio());
                    ps.setInt(6, producto.getExistencias());
                    ps.setString(7, "PROD-" + producto.getId());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO producto (categoria_id, nombre, descripcion, precio, stock, sku) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, catId);
                    ps.setString(2, producto.getNombre());
                    ps.setString(3, producto.getDescripcion() == null ? "" : producto.getDescripcion());
                    ps.setDouble(4, producto.getPrecio());
                    ps.setInt(5, producto.getExistencias());
                    ps.setString(6, "PROD-TEMP");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int nuevoId = rs.getInt(1);
                            producto.setId(nuevoId);
                            try (Statement st = cn.createStatement()) {
                                st.executeUpdate("UPDATE producto SET sku = 'PROD-" + nuevoId + "' WHERE id = " + nuevoId);
                            } catch (SQLException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ProductoDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, categoria_id, nombre, descripcion, precio, stock FROM producto ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error ProductoDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, categoria_id, nombre, descripcion, precio, stock FROM producto WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ProductoDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscar(String nombre, Integer categoriaId) {
        List<Producto> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        StringBuilder sql = new StringBuilder("SELECT id, categoria_id, nombre, descripcion, precio, stock FROM producto WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND LOWER(nombre) LIKE ?");
            params.add("%" + nombre.trim().toLowerCase() + "%");
        }
        if (categoriaId != null && categoriaId > 0) {
            sql.append(" AND categoria_id = ?");
            params.add(categoriaId);
        }
        sql.append(" ORDER BY id ASC");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) {
                    ps.setString(i + 1, (String) p);
                } else if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ProductoDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Producto producto) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        int catId = producto.getCategoriaId() <= 0 ? 1 : producto.getCategoriaId();
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
            System.err.println("Error ProductoDAOImpl.actualizar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void eliminar(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error ProductoDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        return new Producto(
                rs.getInt("id"),
                rs.getInt("categoria_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("stock")
        );
    }
}
