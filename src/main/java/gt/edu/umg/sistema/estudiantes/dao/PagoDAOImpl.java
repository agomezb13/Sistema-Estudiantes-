package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public void guardar(Pago pago) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int pedId = pago.getPedidoId() <= 0 ? 1 : pago.getPedidoId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO pedido (id, cliente_id, estado, total) VALUES (" + pedId + ", 1, 'PENDIENTE', 0.00)");
            } catch (SQLException ignored) {
            }

            if (pago.getId() > 0) {
                if (buscarPorId(pago.getId()) != null) {
                    String sql = "UPDATE pago SET pedido_id = ?, monto = ?, metodo = ?, estado = ? WHERE id = ?";
                    try (PreparedStatement ps = cn.prepareStatement(sql)) {
                        ps.setInt(1, pedId);
                        ps.setDouble(2, pago.getMonto());
                        ps.setString(3, pago.getMetodo() == null ? "EFECTIVO" : pago.getMetodo());
                        ps.setString(4, pago.getEstado() == null ? "PENDIENTE" : pago.getEstado());
                        ps.setInt(5, pago.getId());
                        ps.executeUpdate();
                    }
                    return;
                }
                String sql = "INSERT INTO pago (id, pedido_id, monto, metodo, estado) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, pago.getId());
                    ps.setInt(2, pedId);
                    ps.setDouble(3, pago.getMonto());
                    ps.setString(4, pago.getMetodo() == null ? "EFECTIVO" : pago.getMetodo());
                    ps.setString(5, pago.getEstado() == null ? "PENDIENTE" : pago.getEstado());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO pago (pedido_id, monto, metodo, estado) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, pedId);
                    ps.setDouble(2, pago.getMonto());
                    ps.setString(3, pago.getMetodo() == null ? "EFECTIVO" : pago.getMetodo());
                    ps.setString(4, pago.getEstado() == null ? "PENDIENTE" : pago.getEstado());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pago.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PagoDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Pago> listar() {
        List<Pago> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, pedido_id, monto, metodo, estado FROM pago ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPago(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error PagoDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Pago buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, pedido_id, monto, metodo, estado FROM pago WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PagoDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public Pago buscarPorPedido(int pedidoId) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, pedido_id, monto, metodo, estado FROM pago WHERE pedido_id = ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PagoDAOImpl.buscarPorPedido: " + e.getMessage());
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

        String sql = "DELETE FROM pago WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error PagoDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setId(rs.getInt("id"));
        p.setPedidoId(rs.getInt("pedido_id"));
        p.setMonto(rs.getDouble("monto"));
        p.setMetodo(rs.getString("metodo"));
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
