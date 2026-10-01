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
import java.util.Date;
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
        // Categorías
        Categoria cat1 = new Categoria(siguienteIdCategoria(), "Tecnología", "Dispositivos electrónicos y computación");
        Categoria cat2 = new Categoria(siguienteIdCategoria(), "Hogar y Oficina", "Artículos de escritorio y oficina");
        Categoria cat3 = new Categoria(siguienteIdCategoria(), "Alimentos y Bebidas", "Productos de consumo general");
        categorias.add(cat1);
        categorias.add(cat2);
        categorias.add(cat3);

        // Productos
        Producto p1 = new Producto(siguienteIdProducto(), cat1.getId(), "Laptop HP 15", "Procesador Core i5, 16GB RAM, SSD 512GB", 5200.00, 15);
        Producto p2 = new Producto(siguienteIdProducto(), cat1.getId(), "Mouse Inalámbrico Logitech", "Sensor óptico 1000 DPI", 125.00, 50);
        Producto p3 = new Producto(siguienteIdProducto(), cat1.getId(), "Teclado Mecánico RGB", "Switches azules, conexión USB", 350.00, 30);
        Producto p4 = new Producto(siguienteIdProducto(), cat2.getId(), "Silla Ergonómica Ejecutiva", "Soporte lumbar y altura ajustable", 850.00, 10);
        Producto p5 = new Producto(siguienteIdProducto(), cat2.getId(), "Escritorio para Computadora", "Madera tratada 120x60cm", 750.00, 8);
        Producto p6 = new Producto(siguienteIdProducto(), cat3.getId(), "Café Gourmet en Grano 1lb", "Café de altura guatemalteco", 65.00, 100);
        productos.add(p1);
        productos.add(p2);
        productos.add(p3);
        productos.add(p4);
        productos.add(p5);
        productos.add(p6);

        // Clientes
        Cliente c1 = new Cliente(siguienteIdCliente(), "Juan Carlos Pérez", "juan.perez@gmail.com", "55443322", "Zona 1, Ciudad de Guatemala", "2345678900101", "1234567-8", "ACTIVO");
        Cliente c2 = new Cliente(siguienteIdCliente(), "María Alejandra López", "maria.lopez@yahoo.com", "44332211", "Zona 10, Ciudad de Guatemala", "3456789010101", "8765432-1", "ACTIVO");
        Cliente c3 = new Cliente(siguienteIdCliente(), "Carlos Roberto Gómez", "carlos.gomez@outlook.com", "55667788", "Mixco, Guatemala", "4567890120101", "9988776-5", "ACTIVO");
        clientes.add(c1);
        clientes.add(c2);
        clientes.add(c3);

        // Direcciones de envío
        DireccionEnvio d1 = new DireccionEnvio(siguienteIdDireccion(), c1.getId(), "7ma Avenida 3-45 Zona 1", "Guatemala", "01001", "Guatemala");
        DireccionEnvio d2 = new DireccionEnvio(siguienteIdDireccion(), c2.getId(), "Boulevard Los Próceres Zona 10", "Guatemala", "01010", "Guatemala");
        direcciones.add(d1);
        direcciones.add(d2);

        // Vendedores
        Vendedor v1 = new Vendedor(siguienteIdVendedor(), "Ana Lucía Morales", "ana.morales@ventas.com", "41223344", "ACTIVO");
        Vendedor v2 = new Vendedor(siguienteIdVendedor(), "Pedro Pablo Castillo", "pedro.castillo@ventas.com", "59887766", "ACTIVO");
        vendedores.add(v1);
        vendedores.add(v2);

        // Pedido de ejemplo
        Pedido ped1 = new Pedido(siguienteIdPedido(), c1.getId(), d1.getId(), new Date(), "CONFIRMADO", 5325.00);
        DetallePedido dp1 = new DetallePedido(siguienteIdDetallePedido(), ped1.getId(), p1.getId(), 1, p1.getPrecio(), p1.getPrecio());
        DetallePedido dp2 = new DetallePedido(siguienteIdDetallePedido(), ped1.getId(), p2.getId(), 1, p2.getPrecio(), p2.getPrecio());
        ped1.getDetalles().add(dp1);
        ped1.getDetalles().add(dp2);
        ped1.calcularTotal();
        pedidos.add(ped1);

        // Pago
        Pago pago1 = new Pago(siguienteIdPago(), ped1.getId(), ped1.getTotal(), "TARJETA", "PAGADO");
        pagos.add(pago1);

        // Factura de ejemplo
        Factura f1 = new Factura(siguienteIdFactura(), ped1.getId(), c1.getId(), v1.getId(), "FACT-0001", new Date(), ped1.getTotal(), ped1.getTotal() * 0.12, ped1.getTotal() * 1.12, "EMITIDA");
        DetalleFactura df1 = new DetalleFactura(siguienteIdDetalleFactura(), f1.getId(), p1.getId(), 1, p1.getPrecio(), p1.getPrecio());
        df1.setProducto(p1);
        DetalleFactura df2 = new DetalleFactura(siguienteIdDetalleFactura(), f1.getId(), p2.getId(), 1, p2.getPrecio(), p2.getPrecio());
        df2.setProducto(p2);
        f1.getDetalles().add(df1);
        f1.getDetalles().add(df2);
        f1.calcularTotal();
        facturas.add(f1);
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
