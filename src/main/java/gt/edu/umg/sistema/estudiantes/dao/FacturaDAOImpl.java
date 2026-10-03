package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaDAOImpl implements FacturaDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Factura factura) {
        if (factura.getId() == 0) {
            factura.setId(db.siguienteIdFactura());
            db.getFacturas().add(factura);
        } else {
            Factura actual = buscarPorId(factura.getId());
            if (actual != null) {
                actual.setNumero(factura.getNumero());
                actual.setPedidoId(factura.getPedidoId());
                actual.setClienteId(factura.getClienteId());
                actual.setVendedorId(factura.getVendedorId());
                actual.setFechaEmision(factura.getFechaEmision());
                actual.setSubtotal(factura.getSubtotal());
                actual.setImpuesto(factura.getImpuesto());
                actual.setTotal(factura.getTotal());
                actual.setEstado(factura.getEstado());
                actual.setDetalles(factura.getDetalles());
            } else {
                db.getFacturas().add(factura);
            }
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            int cId = factura.getClienteId() <= 0 ? 1 : factura.getClienteId();
            int vId = factura.getVendedorId() <= 0 ? 1 : factura.getVendedorId();

            try (Statement st = cn.createStatement()) {
                st.executeUpdate("INSERT IGNORE INTO cliente (id, nombre, correo, estado) VALUES (" + cId + ", 'Consumidor Final', 'cf@tienda.com', 'ACTIVO')");
                st.executeUpdate("INSERT IGNORE INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) VALUES (" + vId + ", 'Administrador', 'VEND-001', 'Ventas', '55555555', 'admin@tienda.com', 'ACTIVO')");
            } catch (SQLException ignored) {
            }

            String sql = "INSERT INTO factura (id, cliente_id, vendedor_id, numero, subtotal, impuesto, total, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE subtotal=VALUES(subtotal), impuesto=VALUES(impuesto), total=VALUES(total), estado=VALUES(estado)";
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
            } catch (SQLException e) {
                System.out.println("Aviso al persistir factura en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Factura> listar() {
        return new ArrayList<>(db.getFacturas());
    }

    @Override
    public Factura buscarPorId(int id) {
        for (Factura f : db.getFacturas()) {
            if (f.getId() == id) {
                return f;
            }
        }
        return null;
    }

    @Override
    public List<Factura> buscar(String numero, Integer clienteId) {
        List<Factura> res = new ArrayList<>();
        String numLower = numero == null ? "" : numero.toLowerCase();
        for (Factura f : db.getFacturas()) {
            boolean coincideNum = numLower.isEmpty() || (f.getNumero() != null && f.getNumero().toLowerCase().contains(numLower));
            boolean coincideCliente = clienteId == null || f.getClienteId() == clienteId;
            if (coincideNum && coincideCliente) {
                res.add(f);
            }
        }
        return res;
    }

    @Override
    public void anular(int id) {
        Factura f = buscarPorId(id);
        if (f != null) {
            f.anular();
        }
    }

    @Override
    public void eliminar(int id) {
        db.getFacturas().removeIf(f -> f.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM factura WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar factura en BD: " + e.getMessage());
            }
        }
    }

    @Override
    public void guardarFactura(String nit, String nombre, String direccion, String fechaEmision, String fechaCertificacion, double subtotal, double iva, double total) {
        Cliente cliente = null;
        for (Cliente c : db.getClientes()) {
            if (c.getNIT() != null && c.getNIT().equalsIgnoreCase(nit)) {
                cliente = c;
                break;
            }
        }
        if (cliente == null) {
            cliente = new Cliente(db.siguienteIdCliente(), nombre, "", "", direccion, "", nit, "ACTIVO");
            db.getClientes().add(cliente);
        }

        Factura factura = new Factura();
        factura.setId(db.siguienteIdFactura());
        factura.setClienteId(cliente.getId());
        factura.setNumero("FACT-" + String.format("%04d", factura.getId()));
        factura.setFechaEmision(new Date());
        factura.setSubtotal(subtotal);
        factura.setImpuesto(iva);
        factura.setTotal(total);
        factura.setEstado("EMITIDA");

        guardar(factura);
    }

    @Override
    public void eliminarFactura(String nit) {
        List<Integer> idsClientes = new ArrayList<>();
        for (Cliente c : db.getClientes()) {
            if (c.getNIT() != null && c.getNIT().equalsIgnoreCase(nit)) {
                idsClientes.add(c.getId());
            }
        }
        db.getFacturas().removeIf(f -> idsClientes.contains(f.getClienteId()));

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null && !idsClientes.isEmpty()) {
            String sql = "DELETE FROM factura WHERE cliente_id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                for (int cId : idsClientes) {
                    ps.setInt(1, cId);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar factura en BD: " + e.getMessage());
            }
        }
    }
}
