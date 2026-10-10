package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DireccionEnvioDAOImpl implements DireccionEnvioDAO {

    @Override
    public void guardar(DireccionEnvio direccion) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int cId = direccion.getClienteId() <= 0 ? 1 : direccion.getClienteId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO cliente (id, nombre, correo, estado) VALUES (" + cId + ", 'Consumidor Final', 'cf@tienda.com', 'ACTIVO')");
            } catch (SQLException ignored) {
            }

            if (direccion.getId() > 0) {
                if (buscarPorId(direccion.getId()) != null) {
                    String sql = "UPDATE direccion_envio SET cliente_id = ?, calle = ?, ciudad = ?, codigo_postal = ?, pais = ? WHERE id = ?";
                    try (PreparedStatement ps = cn.prepareStatement(sql)) {
                        ps.setInt(1, cId);
                        ps.setString(2, direccion.getCalle() == null ? "" : direccion.getCalle());
                        ps.setString(3, direccion.getCiudad() == null ? "" : direccion.getCiudad());
                        ps.setString(4, direccion.getCodigoPostal() == null ? "" : direccion.getCodigoPostal());
                        ps.setString(5, direccion.getPais() == null ? "" : direccion.getPais());
                        ps.setInt(6, direccion.getId());
                        ps.executeUpdate();
                    }
                    return;
                }
                String sql = "INSERT INTO direccion_envio (id, cliente_id, calle, ciudad, codigo_postal, pais) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, direccion.getId());
                    ps.setInt(2, cId);
                    ps.setString(3, direccion.getCalle() == null ? "" : direccion.getCalle());
                    ps.setString(4, direccion.getCiudad() == null ? "" : direccion.getCiudad());
                    ps.setString(5, direccion.getCodigoPostal() == null ? "" : direccion.getCodigoPostal());
                    ps.setString(6, direccion.getPais() == null ? "" : direccion.getPais());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO direccion_envio (cliente_id, calle, ciudad, codigo_postal, pais) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, cId);
                    ps.setString(2, direccion.getCalle() == null ? "" : direccion.getCalle());
                    ps.setString(3, direccion.getCiudad() == null ? "" : direccion.getCiudad());
                    ps.setString(4, direccion.getCodigoPostal() == null ? "" : direccion.getCodigoPostal());
                    ps.setString(5, direccion.getPais() == null ? "" : direccion.getPais());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            direccion.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DireccionEnvioDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<DireccionEnvio> listar() {
        List<DireccionEnvio> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, cliente_id, calle, ciudad, codigo_postal, pais FROM direccion_envio ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearDireccion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error DireccionEnvioDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public List<DireccionEnvio> listarPorCliente(int clienteId) {
        List<DireccionEnvio> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, cliente_id, calle, ciudad, codigo_postal, pais FROM direccion_envio WHERE cliente_id = ? ORDER BY id ASC";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDireccion(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DireccionEnvioDAOImpl.listarPorCliente: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public DireccionEnvio buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, cliente_id, calle, ciudad, codigo_postal, pais FROM direccion_envio WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearDireccion(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DireccionEnvioDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public void eliminar(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "DELETE FROM direccion_envio WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DireccionEnvioDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private DireccionEnvio mapearDireccion(ResultSet rs) throws SQLException {
        return new DireccionEnvio(
                rs.getInt("id"),
                rs.getInt("cliente_id"),
                rs.getString("calle"),
                rs.getString("ciudad"),
                rs.getString("codigo_postal"),
                rs.getString("pais")
        );
    }
}
