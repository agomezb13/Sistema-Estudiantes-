package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
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

public class FrmPrincipal extends javax.swing.JFrame {

    private final ContenedorAplicacion contenedor;
    private final String rol;
    private Cliente comprador;
    private FrmEstudiante frmEstudiante;
    private FrmInicio frmInicio;

    public FrmPrincipal() {
        this(new ContenedorAplicacion(), "ADMIN", null);
    }

    public FrmPrincipal(ContenedorAplicacion contenedor) {
        this(contenedor, "ADMIN", null);
    }

    public FrmPrincipal(ContenedorAplicacion contenedor, String rol, Cliente comprador) {
        this.contenedor = contenedor;
        this.rol = (rol != null && !rol.trim().isEmpty()) ? rol.toUpperCase() : "ADMIN";
        this.comprador = comprador;

        initComponents();
        actualizarTitulo();

        setSize(1100, 720);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                salirDirecto();
            }
        });

        setJMenuBar(crearMenuSegunRol());
    }

    private void actualizarTitulo() {
        if ("INVITADO".equals(this.rol) || "COMPRADOR".equals(this.rol)) {
            setTitle("Sistema de Gestión de Ventas - Portal de Invitado");
        } else {
            setTitle("Sistema de Gestión de Ventas - Panel de Administración");
        }
    }

    public void setFrmInicio(FrmInicio frmInicio) {
        this.frmInicio = frmInicio;
    }

    private JMenuBar crearMenuSegunRol() {
        if ("INVITADO".equals(this.rol) || "COMPRADOR".equals(this.rol)) {
            return crearMenuInvitado();
        } else {
            return crearMenuAdmin();
        }
    }

    private JMenuBar crearMenuInvitado() {
        JMenuBar menuBar = new JMenuBar();

        // 1. Órdenes de Compra
        JMenu menuOrdenes = new JMenu("Órdenes de Compra");
        JMenuItem itemMisOrdenes = new JMenuItem("Consultar Órdenes de Compra");
        JMenuItem itemHacerOrden = new JMenuItem("Hacer Nueva Orden de Compra");

        itemMisOrdenes.addActionListener(e -> abrirFormulario(new FrmFiltroPedido(
                contenedor.getPedidoController(),
                contenedor.getClienteController(),
                contenedor.getProductoController(),
                contenedor.getDireccionEnvioController(),
                comprador)));

        itemHacerOrden.addActionListener(e -> abrirFormulario(new FrmTecleoPedido(
                contenedor.getPedidoController(),
                contenedor.getClienteController(),
                contenedor.getProductoController(),
                contenedor.getDireccionEnvioController(),
                null,
                null,
                comprador)));

        menuOrdenes.add(itemMisOrdenes);
        menuOrdenes.add(itemHacerOrden);

        // 2. Stock de Productos
        JMenu menuStock = new JMenu("Stock de Productos");
        JMenuItem itemConsultarStock = new JMenuItem("Consultar Disponibilidad de Stock");
        itemConsultarStock.addActionListener(e -> abrirFormulario(new FrmInventario(
                contenedor.getInventarioController(),
                contenedor.getCategoriaController(),
                true)));
        menuStock.add(itemConsultarStock);

        // 3. Sesión
        JMenu menuSesion = new JMenu("Sesión");
        JMenuItem itemCerrarSesion = new JMenuItem("Cerrar Sesión / Cambiar Usuario");
        JMenuItem itemSalir = new JMenuItem("Salir del Sistema");

        itemCerrarSesion.addActionListener(e -> irAFrmInicio());
        itemSalir.addActionListener(e -> salirDirecto());

        menuSesion.add(itemCerrarSesion);
        menuSesion.add(itemSalir);

        menuBar.add(menuOrdenes);
        menuBar.add(menuStock);
        menuBar.add(menuSesion);

        return menuBar;
    }

    private JMenuBar crearMenuAdmin() {
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
                contenedor.getCategoriaController(),
                false)));
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
        JMenuItem itemClaveAdmin = new JMenuItem("Cambiar Contraseña de Administrador");
        JMenuItem itemInicio = new JMenuItem("Cerrar Sesión / Menú de Inicio");
        JMenuItem itemSalir = new JMenuItem("Salir del Sistema");

        itemClaveAdmin.addActionListener(e -> cambiarClaveAdmin());
        itemInicio.addActionListener(e -> irAFrmInicio());
        itemSalir.addActionListener(e -> salirDirecto());

        menuSistema.add(itemClaveAdmin);
        menuSistema.addSeparator();
        menuSistema.add(itemInicio);
        menuSistema.add(itemSalir);

        menuBar.add(menuCatalogos);
        menuBar.add(menuVentas);
        menuBar.add(menuInventario);
        menuBar.add(menuFacturacion);
        menuBar.add(menuSistema);

        return menuBar;
    }


    private void cambiarClaveAdmin() {
        String actual = JOptionPane.showInputDialog(this, "Ingrese la contraseña actual:", "Cambiar Contraseña", JOptionPane.QUESTION_MESSAGE);
        if (actual == null) return;

        if (!FrmInicio.getClaveAdmin().equals(actual.trim())) {
            JOptionPane.showMessageDialog(this, "La contraseña actual no es correcta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nueva = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña de Administrador:", "Nueva Contraseña", JOptionPane.QUESTION_MESSAGE);
        if (nueva == null || nueva.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La contraseña no puede estar vacía.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FrmInicio.setClaveAdmin(nueva.trim());
        JOptionPane.showMessageDialog(this, "Contraseña de Administrador actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        frmInicio.reiniciar();
        frmInicio.setVisible(true);
        frmInicio.toFront();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Sistema de Gestión de Ventas");

        desktopPane.setBackground(new java.awt.Color(204, 204, 255));
        desktopPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    // End of variables declaration//GEN-END:variables
}
