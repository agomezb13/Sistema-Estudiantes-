package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class FrmPrincipal extends JFrame {

    private final JDesktopPane desktopPane;
    private final ContenedorAplicacion contenedor;
    private FrmEstudiante frmEstudiante;
    private FrmInicio frmInicio;

    public FrmPrincipal(ContenedorAplicacion contenedor) {
        this.contenedor = contenedor;
        setTitle("Sistema de Gestión de Ventas");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                irAFrmFinal();
            }
        });

        desktopPane = new JDesktopPane();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        setJMenuBar(crearMenu());
    }

    public void setFrmInicio(FrmInicio frmInicio) {
        this.frmInicio = frmInicio;
    }

    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        // 1. Catálogos
        JMenu menuCatalogos = new JMenu("Catálogos");
        JMenuItem itemClientes = new JMenuItem("Clientes");
        JMenuItem itemCategorias = new JMenuItem("Categorías");
        JMenuItem itemProductos = new JMenuItem("Productos");
        JMenuItem itemVendedores = new JMenuItem("Vendedores");
        JMenuItem itemAlumnos = new JMenuItem("Gestión Estudiantes");

        itemClientes.addActionListener(e -> abrirFormulario(new FrmFiltroCliente(contenedor.getClienteController())));
        itemCategorias.addActionListener(e -> abrirFormulario(new FrmFiltroCategoria(contenedor.getCategoriaController())));
        itemProductos.addActionListener(e -> abrirFormulario(new FrmFiltroProducto(
                contenedor.getProductoController(), contenedor.getCategoriaController())));
        itemVendedores.addActionListener(e -> abrirFormulario(new FrmFiltroVendedor(contenedor.getVendedorController())));
        itemAlumnos.addActionListener(e -> abrirAlumnos());

        menuCatalogos.add(itemClientes);
        menuCatalogos.add(itemCategorias);
        menuCatalogos.add(itemProductos);
        menuCatalogos.add(itemVendedores);
        menuCatalogos.addSeparator();
        menuCatalogos.add(itemAlumnos);

        // 2. Ventas
        JMenu menuVentas = new JMenu("Ventas");
        JMenuItem itemPedidos = new JMenuItem("Pedidos");
        JMenuItem itemPagos = new JMenuItem("Registro de Pagos");

        itemPedidos.addActionListener(e -> abrirFormulario(new FrmFiltroPedido(
                contenedor.getPedidoController(),
                contenedor.getClienteController(),
                contenedor.getProductoController(),
                contenedor.getDireccionEnvioController())));
        itemPagos.addActionListener(e -> abrirFormulario(new FrmFiltroPago(
                contenedor.getPagoController(),
                contenedor.getPedidoController())));

        menuVentas.add(itemPedidos);
        menuVentas.add(itemPagos);

        // 3. Inventario
        JMenu menuInventario = new JMenu("Inventario");
        JMenuItem itemStock = new JMenuItem("Control de Inventario y Stock");
        itemStock.addActionListener(e -> abrirFormulario(new FrmInventario(
                contenedor.getInventarioController(),
                contenedor.getCategoriaController())));
        menuInventario.add(itemStock);

        // 4. Facturación
        JMenu menuFacturacion = new JMenu("Facturación");
        JMenuItem itemFacturas = new JMenuItem("Facturas");
        itemFacturas.addActionListener(e -> abrirFormulario(new FrmFiltroFactura(
                contenedor.getFacturaController(),
                contenedor.getClienteController(),
                contenedor.getVendedorController(),
                contenedor.getPedidoController(),
                contenedor.getProductoController())));
        menuFacturacion.add(itemFacturas);

        // 5. Sistema
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemInicio = new JMenuItem("Menú de Inicio");
        JMenuItem itemMenuFinal = new JMenuItem("Menú Final / Cierre de Sesión");
        JMenuItem itemSalir = new JMenuItem("Salir del Sistema");

        itemInicio.addActionListener(e -> irAFrmInicio());
        itemMenuFinal.addActionListener(e -> irAFrmFinal());
        itemSalir.addActionListener(e -> salirDirecto());

        menuSistema.add(itemInicio);
        menuSistema.add(itemMenuFinal);
        menuSistema.addSeparator();
        menuSistema.add(itemSalir);

        menuBar.add(menuCatalogos);
        menuBar.add(menuVentas);
        menuBar.add(menuInventario);
        menuBar.add(menuFacturacion);
        menuBar.add(menuSistema);

        return menuBar;
    }

    private void abrirAlumnos() {
        if (frmEstudiante == null || !frmEstudiante.isDisplayable()) {
            frmEstudiante = new FrmEstudiante();
            frmEstudiante.setLocationRelativeTo(this);
        }
        frmEstudiante.setVisible(true);
        frmEstudiante.toFront();
        frmEstudiante.requestFocus();
    }

    public void abrirFormulario(JInternalFrame formulario) {
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            if (frame.getClass().equals(formulario.getClass())) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {
                }
                return;
            }
        }
        desktopPane.add(formulario);
        formulario.setVisible(true);
        try {
            formulario.setSelected(true);
        } catch (Exception ignored) {
        }
    }

    private void irAFrmInicio() {
        setVisible(false);
        if (frmInicio == null) {
            frmInicio = new FrmInicio(contenedor);
        }
        frmInicio.setVisible(true);
        frmInicio.toFront();
    }

    private void irAFrmFinal() {
        setVisible(false);
        FrmFinal frmFinal = new FrmFinal(contenedor, this, frmInicio);
        frmFinal.setVisible(true);
    }

    private void salirDirecto() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir completamente del sistema?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        java.awt.EventQueue.invokeLater(() -> new FrmPrincipal(new ContenedorAplicacion()).setVisible(true));
    }
}
