package gt.edu.umg.sistema.estudiantes.datos;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Carrito;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.DetallePedido;
import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosMemoria {

    private static final BaseDatosMemoria INSTANCIA = new BaseDatosMemoria();

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<DireccionEnvio> direcciones = new ArrayList<>();
    private final List<Categoria> categorias = new ArrayList<>();
    private final List<Producto> productos = new ArrayList<>();
    private final List<Vendedor> vendedores = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();
    private final List<Factura> facturas = new ArrayList<>();
    private final List<Carrito> carritos = new ArrayList<>();

    private int seqDetallePedido = 1;
    private int seqDetalleFactura = 1;
    private int seqCarrito = 1;

    private BaseDatosMemoria() {
        cargarDatosDesdeBD();
    }

    public static BaseDatosMemoria getInstancia() {
        return INSTANCIA;
    }

    public synchronized void cargarDatosDesdeBD() {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }
        try {
            // Cargar clientes
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, nombre, correo, telefono, direccion, numero_dpi, nit, estado FROM cliente ORDER BY id ASC")) {
                clientes.clear();
                while (rs.next()) {
                    clientes.add(new Cliente(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("numero_dpi"),
                            rs.getString("nit"),
                            rs.getString("estado")
                    ));
                }
            } catch (SQLException ignored) {
            }

            // Cargar categorias
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, nombre, descripcion FROM categoria ORDER BY id ASC")) {
                categorias.clear();
                while (rs.next()) {
                    categorias.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
                }
            } catch (SQLException ignored) {
            }

            // Cargar productos
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, categoria_id, nombre, descripcion, precio, stock FROM producto ORDER BY id ASC")) {
                productos.clear();
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getInt("id"),
                            rs.getInt("categoria_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    ));
                }
            } catch (SQLException ignored) {
            }

            // Cargar vendedores
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, nombre, correo, telefono, estado FROM vendedor ORDER BY id ASC")) {
                vendedores.clear();
                while (rs.next()) {
                    vendedores.add(new Vendedor(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getString("estado")
                    ));
                }
            } catch (SQLException ignored) {
            }

            // Cargar pedidos
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, cliente_id, direccion_envio_id, fecha, estado, total FROM pedido ORDER BY id ASC")) {
                pedidos.clear();
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
                    pedidos.add(p);
                }
            } catch (SQLException ignored) {
            }

            // Cargar facturas
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, pedido_id, cliente_id, vendedor_id, numero, fecha_emision, subtotal, impuesto, total, estado FROM factura ORDER BY id ASC")) {
                facturas.clear();
                while (rs.next()) {
                    Factura f = new Factura();
                    f.setId(rs.getInt("id"));
                    int pedId = rs.getInt("pedido_id");
                    if (!rs.wasNull()) f.setPedidoId(pedId);
                    f.setClienteId(rs.getInt("cliente_id"));
                    f.setVendedorId(rs.getInt("vendedor_id"));
                    f.setNumero(rs.getString("numero"));
                    f.setFechaEmision(rs.getTimestamp("fecha_emision"));
                    f.setSubtotal(rs.getDouble("subtotal"));
                    f.setImpuesto(rs.getDouble("impuesto"));
                    f.setTotal(rs.getDouble("total"));
                    f.setEstado(rs.getString("estado"));
                    facturas.add(f);
                }
            } catch (SQLException ignored) {
            }

            // Cargar pagos
            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, pedido_id, monto, metodo, estado FROM pago ORDER BY id ASC")) {
                pagos.clear();
                while (rs.next()) {
                    Pago p = new Pago();
                    p.setId(rs.getInt("id"));
                    p.setPedidoId(rs.getInt("pedido_id"));
                    p.setMonto(rs.getDouble("monto"));
                    p.setMetodo(rs.getString("metodo"));
                    p.setEstado(rs.getString("estado"));
                    pagos.add(p);
                }
            } catch (SQLException ignored) {
            }

        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private int maxIdEnBD(String tabla) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) return 0;
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(MAX(id), 0) FROM " + tabla)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ignored) {
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return 0;
    }

    public synchronized int siguienteIdCliente() {
        int max = 0;
        for (Cliente c : clientes) {
            if (c.getId() > max) max = c.getId();
        }
        max = Math.max(max, maxIdEnBD("cliente"));
        return max + 1;
    }

    public synchronized int siguienteIdDireccion() {
        int max = 0;
        for (DireccionEnvio d : direcciones) {
            if (d.getId() > max) max = d.getId();
        }
        max = Math.max(max, maxIdEnBD("direccion_envio"));
        return max + 1;
    }

    public synchronized int siguienteIdCategoria() {
        int max = 0;
        for (Categoria c : categorias) {
            if (c.getId() > max) max = c.getId();
        }
        max = Math.max(max, maxIdEnBD("categoria"));
        return max + 1;
    }

    public synchronized int siguienteIdProducto() {
        int max = 0;
        for (Producto p : productos) {
            if (p.getId() > max) max = p.getId();
        }
        max = Math.max(max, maxIdEnBD("producto"));
        return max + 1;
    }

    public synchronized int siguienteIdVendedor() {
        int max = 0;
        for (Vendedor v : vendedores) {
            if (v.getId() > max) max = v.getId();
        }
        max = Math.max(max, maxIdEnBD("vendedor"));
        return max + 1;
    }

    public synchronized int siguienteIdPedido() {
        int max = 0;
        for (Pedido p : pedidos) {
            if (p.getId() > max) max = p.getId();
        }
        max = Math.max(max, maxIdEnBD("pedido"));
        return max + 1;
    }

    public synchronized int siguienteIdDetallePedido() {
        return seqDetallePedido++;
    }

    public synchronized int siguienteIdPago() {
        int max = 0;
        for (Pago p : pagos) {
            if (p.getId() > max) max = p.getId();
        }
        max = Math.max(max, maxIdEnBD("pago"));
        return max + 1;
    }

    public synchronized int siguienteIdFactura() {
        int max = 0;
        for (Factura f : facturas) {
            if (f.getId() > max) max = f.getId();
        }
        max = Math.max(max, maxIdEnBD("factura"));
        return max + 1;
    }

    public synchronized int siguienteIdDetalleFactura() {
        return seqDetalleFactura++;
    }

    public synchronized int siguienteIdCarrito() {
        return seqCarrito++;
    }

    public List<Cliente> getClientes() { return clientes; }
    public List<DireccionEnvio> getDirecciones() { return direcciones; }
    public List<Categoria> getCategorias() { return categorias; }
    public List<Producto> getProductos() { return productos; }
    public List<Vendedor> getVendedores() { return vendedores; }
    public List<Pedido> getPedidos() { return pedidos; }
    public List<Pago> getPagos() { return pagos; }
    public List<Factura> getFacturas() { return facturas; }
    public List<Carrito> getCarritos() { return carritos; }
}
