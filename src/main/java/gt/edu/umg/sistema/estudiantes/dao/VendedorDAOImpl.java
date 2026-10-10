package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAOImpl implements VendedorDAO {

    @Override
    public void guardar(Vendedor vendedor) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            if (vendedor.getId() > 0) {
                if (buscarPorId(vendedor.getId()) != null) {
                    actualizar(vendedor);
                    return;
                }
                String sql = "INSERT INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, vendedor.getId());
                    ps.setString(2, vendedor.getNombre());
                    ps.setString(3, "VEND-" + String.format("%03d", vendedor.getId()));
                    ps.setString(4, "Ventas");
                    ps.setString(5, vendedor.getTelefono() == null ? "" : vendedor.getTelefono());
                    ps.setString(6, vendedor.getCorreo() == null ? "" : vendedor.getCorreo());
                    ps.setString(7, vendedor.getEstado() == null ? "ACTIVO" : vendedor.getEstado());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO vendedor (nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, vendedor.getNombre());
                    ps.setString(2, "VEND-TEMP");
                    ps.setString(3, "Ventas");
                    ps.setString(4, vendedor.getTelefono() == null ? "" : vendedor.getTelefono());
                    ps.setString(5, vendedor.getCorreo() == null ? "" : vendedor.getCorreo());
                    ps.setString(6, vendedor.getEstado() == null ? "ACTIVO" : vendedor.getEstado());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int nuevoId = rs.getInt(1);
                            vendedor.setId(nuevoId);
                            try (Statement st = cn.createStatement()) {
                                st.executeUpdate("UPDATE vendedor SET codigo_empleado = 'VEND-" + String.format("%03d", nuevoId) + "' WHERE id = " + nuevoId);
                            } catch (SQLException ignored) {
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Vendedor> listar() {
        List<Vendedor> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, nombre, correo, telefono, estado FROM vendedor ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearVendedor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Vendedor buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, nombre, correo, telefono, estado FROM vendedor WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearVendedor(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Vendedor> buscar(String nombre, String correo) {
        List<Vendedor> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        StringBuilder sql = new StringBuilder("SELECT id, nombre, correo, telefono, estado FROM vendedor WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND LOWER(nombre) LIKE ?");
            params.add("%" + nombre.trim().toLowerCase() + "%");
        }
        if (correo != null && !correo.trim().isEmpty()) {
            sql.append(" AND LOWER(correo) LIKE ?");
            params.add("%" + correo.trim().toLowerCase() + "%");
        }
        sql.append(" ORDER BY id ASC");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearVendedor(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Vendedor vendedor) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE vendedor SET nombre = ?, telefono = ?, correo = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, vendedor.getNombre());
            ps.setString(2, vendedor.getTelefono() == null ? "" : vendedor.getTelefono());
            ps.setString(3, vendedor.getCorreo() == null ? "" : vendedor.getCorreo());
            ps.setString(4, vendedor.getEstado() == null ? "ACTIVO" : vendedor.getEstado());
            ps.setInt(5, vendedor.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.actualizar: " + e.getMessage());
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

        String sql = "DELETE FROM vendedor WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error VendedorDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Vendedor mapearVendedor(ResultSet rs) throws SQLException {
        return new Vendedor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("estado")
        );
    }
}
