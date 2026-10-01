package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    }

    @Override
    public void guardarFactura(String nit, String nombre, String direccion, String fechaEmision, String fechaCertificacion, double subtotal, double iva, double total) {
        // Guardar en memoria
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
        db.getFacturas().add(factura);

        // Guardar en base de datos si hay conexion
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "INSERT INTO factura (nit_receptor, nombre_cliente, direccion, fecha_emision, fecha_certificacion, subtotal, iva, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, nit);
                ps.setString(2, nombre);
                ps.setString(3, direccion);
                ps.setString(4, fechaEmision);
                ps.setString(5, fechaCertificacion);
                ps.setDouble(6, subtotal);
                ps.setDouble(7, iva);
                ps.setDouble(8, total);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al guardar factura en BD: " + e.getMessage());
            }
        }
    }

    @Override
    public void eliminarFactura(String nit) {
        // En memoria
        List<Integer> idsClientes = new ArrayList<>();
        for (Cliente c : db.getClientes()) {
            if (c.getNIT() != null && c.getNIT().equalsIgnoreCase(nit)) {
                idsClientes.add(c.getId());
            }
        }
        db.getFacturas().removeIf(f -> idsClientes.contains(f.getClienteId()));

        // En BD
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM factura WHERE nit_receptor = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, nit);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error al eliminar factura en BD: " + e.getMessage());
            }
        }
    }
}
