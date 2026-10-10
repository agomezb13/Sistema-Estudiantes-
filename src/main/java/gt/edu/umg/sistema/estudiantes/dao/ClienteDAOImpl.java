package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public void guardar(Cliente cliente) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            if (cliente.getId() > 0) {
                if (buscarPorId(cliente.getId()) != null) {
                    actualizar(cliente);
                    return;
                }
                String sql = "INSERT INTO cliente (id, nombre, correo, telefono, direccion, numero_dpi, nit, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, cliente.getId());
                    ps.setString(2, cliente.getNombre());
                    ps.setString(3, cliente.getCorreo() == null ? "" : cliente.getCorreo());
                    ps.setString(4, cliente.getTelefono() == null ? "" : cliente.getTelefono());
                    ps.setString(5, cliente.getDireccion() == null ? "" : cliente.getDireccion());
                    ps.setString(6, cliente.getNumeroDPI() == null ? "" : cliente.getNumeroDPI());
                    ps.setString(7, cliente.getNIT() == null ? "" : cliente.getNIT());
                    ps.setString(8, cliente.getEstado() == null ? "ACTIVO" : cliente.getEstado());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO cliente (nombre, correo, telefono, direccion, numero_dpi, nit, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, cliente.getNombre());
                    ps.setString(2, cliente.getCorreo() == null ? "" : cliente.getCorreo());
                    ps.setString(3, cliente.getTelefono() == null ? "" : cliente.getTelefono());
                    ps.setString(4, cliente.getDireccion() == null ? "" : cliente.getDireccion());
                    ps.setString(5, cliente.getNumeroDPI() == null ? "" : cliente.getNumeroDPI());
                    ps.setString(6, cliente.getNIT() == null ? "" : cliente.getNIT());
                    ps.setString(7, cliente.getEstado() == null ? "ACTIVO" : cliente.getEstado());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            cliente.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, nombre, correo, telefono, direccion, numero_dpi, nit, estado FROM cliente ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, nombre, correo, telefono, direccion, numero_dpi, nit, estado FROM cliente WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Cliente> buscar(String nit, String nombre) {
        List<Cliente> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        StringBuilder sql = new StringBuilder("SELECT id, nombre, correo, telefono, direccion, numero_dpi, nit, estado FROM cliente WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (nit != null && !nit.trim().isEmpty()) {
            sql.append(" AND LOWER(nit) LIKE ?");
            params.add("%" + nit.trim().toLowerCase() + "%");
        }
        if (nombre != null && !nombre.trim().isEmpty()) {
            sql.append(" AND LOWER(nombre) LIKE ?");
            params.add("%" + nombre.trim().toLowerCase() + "%");
        }
        sql.append(" ORDER BY id ASC");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Cliente cliente) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE cliente SET nombre = ?, correo = ?, telefono = ?, direccion = ?, numero_dpi = ?, nit = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getCorreo() == null ? "" : cliente.getCorreo());
            ps.setString(3, cliente.getTelefono() == null ? "" : cliente.getTelefono());
            ps.setString(4, cliente.getDireccion() == null ? "" : cliente.getDireccion());
            ps.setString(5, cliente.getNumeroDPI() == null ? "" : cliente.getNumeroDPI());
            ps.setString(6, cliente.getNIT() == null ? "" : cliente.getNIT());
            ps.setString(7, cliente.getEstado() == null ? "ACTIVO" : cliente.getEstado());
            ps.setInt(8, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.actualizar: " + e.getMessage());
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

        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error ClienteDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("direccion"),
                rs.getString("numero_dpi"),
                rs.getString("nit"),
                rs.getString("estado")
        );
    }
}
