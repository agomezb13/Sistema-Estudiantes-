package gt.edu.umg.sistema.estudiantes.datos;

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

    private int seqCliente = 1;
    private int seqDireccion = 1;
    private int seqCategoria = 1;
    private int seqProducto = 1;
    private int seqVendedor = 1;
    private int seqPedido = 1;
    private int seqDetallePedido = 1;
    private int seqPago = 1;
    private int seqFactura = 1;
    private int seqDetalleFactura = 1;
    private int seqCarrito = 1;

    private BaseDatosMemoria() {
        cargarDatosIniciales();
    }

    public static BaseDatosMemoria getInstancia() {
        return INSTANCIA;
    }

    private void cargarDatosIniciales() {
    }

    public synchronized int siguienteIdCliente() { return seqCliente++; }
    public synchronized int siguienteIdDireccion() { return seqDireccion++; }
    public synchronized int siguienteIdCategoria() { return seqCategoria++; }
    public synchronized int siguienteIdProducto() { return seqProducto++; }
    public synchronized int siguienteIdVendedor() { return seqVendedor++; }
    public synchronized int siguienteIdPedido() { return seqPedido++; }
    public synchronized int siguienteIdDetallePedido() { return seqDetallePedido++; }
    public synchronized int siguienteIdPago() { return seqPago++; }
    public synchronized int siguienteIdFactura() { return seqFactura++; }
    public synchronized int siguienteIdDetalleFactura() { return seqDetalleFactura++; }
    public synchronized int siguienteIdCarrito() { return seqCarrito++; }

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
