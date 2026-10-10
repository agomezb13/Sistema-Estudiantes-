package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAOImpl implements FacturaDAO {

    @Override
    public void guardar(Factura factura) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int cId = factura.getClienteId() <= 0 ? 1 : factura.getClienteId();
            int vId = factura.getVendedorId() <= 0 ? 1 : factura.getVendedorId();

            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO cliente (id, nombre, correo, estado) VALUES (" + cId + ", 'Consumidor Final', 'cf@tienda.com', 'ACTIVO')");
                st.executeUpdate("INSERT IGNORE INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES (" + vId + ", 'Administrador', 'VEND-001', 'Ventas', '55555555', 'admin@tienda.com', 'ACTIVO')");
            } catch (SQLException ignored) {
            }

            if (factura.getId() > 0) {
                if (buscarPorId(factura.getId()) != null) {
                    String sql = "UPDATE factura SET cliente_id = ?, vendedor_id = ?, numero = ?, subtotal = ?, impuesto = ?, total = ?, estado = ? WHERE id = ?";
                    try (PreparedStatement ps = cn.prepareStatement(sql)) {
                        ps.setInt(1, cId);
                        ps.setInt(2, vId);
                        ps.setString(3, factura.getNumero() == null ? "FACT-" + factura.getId() : factura.getNumero());
                        ps.setDouble(4, factura.getSubtotal());
                        ps.setDouble(5, factura.getImpuesto());
                        ps.setDouble(6, factura.getTotal());
                        ps.setString(7, factura.getEstado() == null ? "EMITIDA" : factura.getEstado());
                        ps.setInt(8, factura.getId());
                        ps.executeUpdate();
                    }
                    return;
                }
                String sql = "INSERT INTO factura (id, cliente_id, vendedor_id, numero, subtotal, impuesto, total, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, factura.getId());
                    ps.setInt(2, cId);
                    ps.setInt(3, vId);
                    ps.setString(4, factura.getNumero() == null ? "FACT-" + factura.getId() : factura.getNumero());
                    ps.setDouble(5, factura.getSubtotal());
                    ps.setDouble(6, factura.getImpuesto());
                    ps.setDouble(7, factura.getTotal());
                    ps.setString(8, factura.getEstado() == null ? "EMITIDA" : factura.getEstado());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO factura (cliente_id, vendedor_id, numero, subtotal, impuesto, total, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, cId);
                    ps.setInt(2, vId);
                    ps.setString(3, factura.getNumero() == null ? "FACT-TEMP" : factura.getNumero());
                    ps.setDouble(4, factura.getSubtotal());
                    ps.setDouble(5, factura.getImpuesto());
                    ps.setDouble(6, factura.getTotal());
                    ps.setString(7, factura.getEstado() == null ? "EMITIDA" : factura.getEstado());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int nuevoId = rs.getInt(1);
                            factura.setId(nuevoId);
                            if (factura.getNumero() == null || factura.getNumero().equals("FACT-TEMP")) {
                                factura.setNumero("FACT-" + String.format("%04d", nuevoId));
                                try (Statement st = cn.createStatement()) {
                                    st.executeUpdate("UPDATE factura SET numero = '" + factura.getNumero() + "' WHERE id = " + nuevoId);
                                } catch (SQLException ignored) {
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Factura> listar() {
        List<Factura> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, pedido_id, cliente_id, vendedor_id, numero, fecha_emision, subtotal, impuesto, total, estado FROM factura ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearFactura(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Factura buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, pedido_id, cliente_id, vendedor_id, numero, fecha_emision, subtotal, impuesto, total, estado FROM factura WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFactura(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Factura> buscar(String numero, Integer clienteId) {
        List<Factura> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        StringBuilder sql = new StringBuilder("SELECT id, pedido_id, cliente_id, vendedor_id, numero, fecha_emision, subtotal, impuesto, total, estado FROM factura WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (numero != null && !numero.trim().isEmpty()) {
            sql.append(" AND LOWER(numero) LIKE ?");
            params.add("%" + numero.trim().toLowerCase() + "%");
        }
        if (clienteId != null && clienteId > 0) {
            sql.append(" AND cliente_id = ?");
            params.add(clienteId);
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
                    lista.add(mapearFactura(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void anular(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE factura SET estado = 'ANULADA' WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.anular: " + e.getMessage());
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

        String sql = "DELETE FROM factura WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void guardarFactura(String nit, String nombre, String direccion, String fechaEmision, String fechaCertificacion, double subtotal, double iva, double total) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            int clienteId = 1;
            String sqlCli = "SELECT id FROM cliente WHERE LOWER(nit) = ? LIMIT 1";
            try (PreparedStatement psCli = cn.prepareStatement(sqlCli)) {
                psCli.setString(1, nit == null ? "" : nit.trim().toLowerCase());
                try (ResultSet rs = psCli.executeQuery()) {
                    if (rs.next()) {
                        clienteId = rs.getInt("id");
                    } else {
                        String insCli = "INSERT INTO cliente (nombre, correo, direccion, nit, estado) VALUES (?, '', ?, ?, 'ACTIVO')";
                        try (PreparedStatement psIns = cn.prepareStatement(insCli, Statement.RETURN_GENERATED_KEYS)) {
                            psIns.setString(1, nombre == null ? "Cliente " + nit : nombre);
                            psIns.setString(2, direccion == null ? "" : direccion);
                            psIns.setString(3, nit == null ? "" : nit);
                            psIns.executeUpdate();
                            try (ResultSet rsKey = psIns.getGeneratedKeys()) {
                                if (rsKey.next()) {
                                    clienteId = rsKey.getInt(1);
                                }
                            }
                        }
                    }
                }
            }

            Factura f = new Factura();
            f.setClienteId(clienteId);
            f.setVendedorId(1);
            f.setSubtotal(subtotal);
            f.setImpuesto(iva);
            f.setTotal(total);
            f.setEstado("EMITIDA");
            guardar(f);
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.guardarFactura: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void eliminarFactura(String nit) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            String sql = "DELETE f FROM factura f INNER JOIN cliente c ON f.cliente_id = c.id WHERE LOWER(c.nit) = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, nit == null ? "" : nit.trim().toLowerCase());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error FacturaDAOImpl.eliminarFactura: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private Factura mapearFactura(ResultSet rs) throws SQLException {
        Factura f = new Factura();
        f.setId(rs.getInt("id"));
        int pedId = rs.getInt("pedido_id");
        if (!rs.wasNull()) {
            f.setPedidoId(pedId);
        }
        f.setClienteId(rs.getInt("cliente_id"));
        f.setVendedorId(rs.getInt("vendedor_id"));
        f.setNumero(rs.getString("numero"));
        f.setFechaEmision(rs.getTimestamp("fecha_emision"));
        f.setSubtotal(rs.getDouble("subtotal"));
        f.setImpuesto(rs.getDouble("impuesto"));
        f.setTotal(rs.getDouble("total"));
        f.setEstado(rs.getString("estado"));
        return f;
    }
}
