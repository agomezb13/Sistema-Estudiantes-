package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public void guardar(Pedido pedido) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int cId = pedido.getClienteId() <= 0 ? 1 : pedido.getClienteId();
            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO cliente (id, nombre, correo, estado) VALUES (" + cId + ", 'Consumidor Final', 'cf@tienda.com', 'ACTIVO')");
            } catch (SQLException ignored) {
            }

            if (pedido.getId() > 0) {
                if (buscarPorId(pedido.getId()) != null) {
                    actualizar(pedido);
                    return;
                }
                String sql = "INSERT INTO pedido (id, cliente_id, direccion_envio_id, estado, total) VALUES (?, ?, ?, ?, ?)";
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
                }
            } else {
                String sql = "INSERT INTO pedido (cliente_id, direccion_envio_id, estado, total) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, cId);
                    if (pedido.getDireccionEnvioId() > 0) {
                        ps.setInt(2, pedido.getDireccionEnvioId());
                    } else {
                        ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    ps.setString(3, pedido.getEstado() == null ? "PENDIENTE" : pedido.getEstado());
                    ps.setDouble(4, pedido.getTotal());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, cliente_id, direccion_envio_id, fecha, estado, total FROM pedido ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Pedido buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, cliente_id, direccion_envio_id, fecha, estado, total FROM pedido WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Pedido> buscar(Integer clienteId, String estado) {
        List<Pedido> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        StringBuilder sql = new StringBuilder("SELECT id, cliente_id, direccion_envio_id, fecha, estado, total FROM pedido WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (clienteId != null && clienteId > 0) {
            sql.append(" AND cliente_id = ?");
            params.add(clienteId);
        }
        if (estado != null && !estado.trim().isEmpty()) {
            sql.append(" AND LOWER(estado) LIKE ?");
            params.add("%" + estado.trim().toLowerCase() + "%");
        }
        sql.append(" ORDER BY id ASC");

        try (PreparedStatement ps = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                } else if (p instanceof String) {
                    ps.setString(i + 1, (String) p);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPedido(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Pedido pedido) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE pedido SET cliente_id = ?, direccion_envio_id = ?, estado = ?, total = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, pedido.getClienteId() <= 0 ? 1 : pedido.getClienteId());
            if (pedido.getDireccionEnvioId() > 0) {
                ps.setInt(2, pedido.getDireccionEnvioId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, pedido.getEstado() == null ? "PENDIENTE" : pedido.getEstado());
            ps.setDouble(4, pedido.getTotal());
            ps.setInt(5, pedido.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.actualizar: " + e.getMessage());
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

        String sql = "DELETE FROM pedido WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error PedidoDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
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
        return p;
    }
}
