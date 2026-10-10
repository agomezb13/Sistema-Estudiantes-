package gt.edu.umg.sistema.estudiantes.config;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.controlador.DireccionEnvioController;
import gt.edu.umg.sistema.estudiantes.controlador.FacturaController;
import gt.edu.umg.sistema.estudiantes.controlador.InventarioController;
import gt.edu.umg.sistema.estudiantes.controlador.PagoController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.controlador.VendedorController;
import gt.edu.umg.sistema.estudiantes.dao.CategoriaDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.ClienteDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.DireccionEnvioDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.PagoDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.PedidoDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.ProductoDAOImpl;
import gt.edu.umg.sistema.estudiantes.dao.VendedorDAOImpl;

/**
 * Contenedor de la aplicacion (Inyeccion de Dependencias sencilla).
 * 
 * En el patron MVC y DAO:
 * - Aqui se crean las instancias concretas de los DAOs (ClienteDAOImpl, ProductoDAOImpl, etc.).
 * - Se inyectan a cada Controlador (ClienteController, ProductoController, etc.).
 * - Las Vistas (formularios Swing) solicitan los controladores a traves de este contenedor.
 * Esto evita acoplamiento directo entre las vistas y el acceso a la base de datos.
 */
public class ContenedorAplicacion {

    private final ClienteController clienteController;
    private final CategoriaController categoriaController;
    private final ProductoController productoController;
    private final VendedorController vendedorController;
    private final PedidoController pedidoController;
    private final PagoController pagoController;
    private final FacturaController facturaController;
    private final InventarioController inventarioController;
    private final DireccionEnvioController direccionEnvioController;

    public ContenedorAplicacion() {
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();
        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        VendedorDAOImpl vendedorDAO = new VendedorDAOImpl();
        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();
        PagoDAOImpl pagoDAO = new PagoDAOImpl();
        FacturaDAOImpl facturaDAO = new FacturaDAOImpl();
        DireccionEnvioDAOImpl direccionDAO = new DireccionEnvioDAOImpl();

        clienteController = new ClienteController(clienteDAO);
        categoriaController = new CategoriaController(categoriaDAO);
        productoController = new ProductoController(productoDAO);
        vendedorController = new VendedorController(vendedorDAO);
        pedidoController = new PedidoController(pedidoDAO);
        pagoController = new PagoController(pagoDAO);
        facturaController = new FacturaController(facturaDAO);
        inventarioController = new InventarioController(productoDAO);
        direccionEnvioController = new DireccionEnvioController(direccionDAO);
    }

    public ClienteController getClienteController() {
        return clienteController;
    }

    public CategoriaController getCategoriaController() {
        return categoriaController;
    }

    public ProductoController getProductoController() {
        return productoController;
    }

    public VendedorController getVendedorController() {
        return vendedorController;
    }

    public PedidoController getPedidoController() {
        return pedidoController;
    }

    public PagoController getPagoController() {
        return pagoController;
    }

    public FacturaController getFacturaController() {
        return facturaController;
    }

    public InventarioController getInventarioController() {
        return inventarioController;
    }

    public DireccionEnvioController getDireccionEnvioController() {
        return direccionEnvioController;
    }
}
