package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Pedido pedido) {
        boolean esNuevo = (pedido.getId() <= 0) || (buscarPorId(pedido.getId()) == null);

        if (pedido.getId() <= 0) {
            pedido.setId(db.siguienteIdPedido());
        }

        if (esNuevo) {
            db.getPedidos().removeIf(p -> p.getId() == pedido.getId());
            db.getPedidos().add(pedido);
        } else {
            actualizar(pedido);
            return;
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            int cId = pedido.getClienteId() <= 0 ? 1 : pedido.getClienteId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO cliente (id, nombre, correo, estado) VALUES (" + cId + ", 'Consumidor Final', 'cf@tienda.com', 'ACTIVO')");
            } catch (SQLException ignored) {
            }

            String sql = "INSERT INTO pedido (id, cliente_id, direccion_envio_id, estado, total) VALUES (?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE estado=VALUES(estado), total=VALUES(total)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, pedido.getId());
                ps.setInt(2, cId);
                if (pedido.getDireccionEnvioId() > 0) {
                    ps.setInt(3, pedido.getDireccionEnvioId());
                } else {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                ps.setString(4, pedido.getEstado() == null ? "PENDIENTE" : pedido.getEstado());
                ps.setDouble(5, pedido.getTotal());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al persistir pedido en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Pedido> listar() {
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, cliente_id, direccion_envio_id, fecha, estado, total FROM pedido ORDER BY id ASC";
            try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                List<Pedido> desdeBD = new ArrayList<>();
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id"));
                    p.setClienteId(rs.getInt("cliente_id"));
                    int dirId = rs.getInt("direccion_envio_id");
                    if (!rs.wasNull()) {
                        p.setDireccionEnvioId(dirId);
                    }
                    p.setFecha(rs.getTimestamp("fecha"));
                    p.setEstado(rs.getString("estado"));
                    p.setTotal(rs.getDouble("total"));
                    desdeBD.add(p);
                }
                if (!desdeBD.isEmpty()) {
                    db.getPedidos().clear();
                    db.getPedidos().addAll(desdeBD);
                    return desdeBD;
                }
            } catch (SQLException e) {
                System.out.println("Aviso al listar pedidos de BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return new ArrayList<>(db.getPedidos());
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido p : listar()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Pedido> buscar(Integer clienteId, String estado) {
        List<Pedido> res = new ArrayList<>();
        String estLower = estado == null ? "" : estado.toLowerCase();
        for (Pedido p : listar()) {
            boolean coincideCliente = clienteId == null || p.getClienteId() == clienteId;
            boolean coincideEstado = estLower.isEmpty() || (p.getEstado() != null && p.getEstado().toLowerCase().contains(estLower));
            if (coincideCliente && coincideEstado) {
                res.add(p);
            }
        }
        return res;
    }

    @Override
    public void actualizar(Pedido pedido) {
        Pedido actual = null;
        for (Pedido p : db.getPedidos()) {
            if (p.getId() == pedido.getId()) {
                actual = p;
                break;
            }
        }
        if (actual == null) {
            db.getPedidos().add(pedido);
        } else {
            actual.setClienteId(pedido.getClienteId());
            actual.setDireccionEnvioId(pedido.getDireccionEnvioId());
            actual.setFecha(pedido.getFecha());
            actual.setEstado(pedido.getEstado());
            actual.setTotal(pedido.getTotal());
            actual.setDetalles(pedido.getDetalles());
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "UPDATE pedido SET estado = ?, total = ? WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, pedido.getEstado());
                ps.setDouble(2, pedido.getTotal());
                ps.setInt(3, pedido.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al actualizar pedido en BD: " + e.getMessage());
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
        db.getPedidos().removeIf(p -> p.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM pedido WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar pedido en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
