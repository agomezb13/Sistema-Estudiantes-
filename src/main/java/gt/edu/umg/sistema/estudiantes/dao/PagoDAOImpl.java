package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Pago pago) {
        boolean esNuevo = (pago.getId() <= 0) || (buscarPorId(pago.getId()) == null);

        if (pago.getId() <= 0) {
            pago.setId(db.siguienteIdPago());
        }

        if (esNuevo) {
            db.getPagos().removeIf(p -> p.getId() == pago.getId());
            db.getPagos().add(pago);
        } else {
            Pago actual = buscarPorId(pago.getId());
            if (actual != null && actual != pago) {
                actual.setPedidoId(pago.getPedidoId());
                actual.setMonto(pago.getMonto());
                actual.setMetodo(pago.getMetodo());
                actual.setEstado(pago.getEstado());
            }
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "INSERT INTO pago (id, pedido_id, monto, metodo, estado) VALUES (?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE pedido_id=VALUES(pedido_id), monto=VALUES(monto), metodo=VALUES(metodo), estado=VALUES(estado)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, pago.getId());
                ps.setInt(2, pago.getPedidoId());
                ps.setDouble(3, pago.getMonto());
                ps.setString(4, pago.getMetodo() == null ? "EFECTIVO" : pago.getMetodo());
                ps.setString(5, pago.getEstado() == null ? "PENDIENTE" : pago.getEstado());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al persistir pago en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Pago> listar() {
        if (db.getPagos().isEmpty()) {
            db.cargarDatosDesdeBD();
        }
        return new ArrayList<>(db.getPagos());
    }

    @Override
    public Pago buscarPorId(int id) {
        for (Pago p : listar()) {
            if (p.getId() == id) {
                return p;
            }
        }
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, pedido_id, monto, metodo, estado FROM pago WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Pago p = new Pago();
                        p.setId(rs.getInt("id"));
                        p.setPedidoId(rs.getInt("pedido_id"));
                        p.setMonto(rs.getDouble("monto"));
                        p.setMetodo(rs.getString("metodo"));
                        p.setEstado(rs.getString("estado"));
                        db.getPagos().add(p);
                        return p;
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
    public Pago buscarPorPedido(int pedidoId) {
        for (Pago p : listar()) {
            if (p.getPedidoId() == pedidoId) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void eliminar(int id) {
        db.getPagos().removeIf(p -> p.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM pago WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar pago en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
