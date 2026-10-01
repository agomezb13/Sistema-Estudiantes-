package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class FrmFinal extends JFrame {

    private final ContenedorAplicacion contenedor;
    private final FrmPrincipal frmPrincipal;
    private final FrmInicio frmInicio;
    private final String rol;
    private final Cliente comprador;

    public FrmFinal(ContenedorAplicacion contenedor, FrmPrincipal frmPrincipal, FrmInicio frmInicio) {
        this(contenedor, frmPrincipal, frmInicio, "ADMIN", null);
    }

    public FrmFinal(ContenedorAplicacion contenedor, FrmPrincipal frmPrincipal, FrmInicio frmInicio, String rol, Cliente comprador) {
        this.contenedor = contenedor;
        this.frmPrincipal = frmPrincipal;
        this.frmInicio = frmInicio;
        this.rol = (rol != null && !rol.trim().isEmpty()) ? rol.toUpperCase() : "ADMIN";
        this.comprador = comprador;

        setTitle("Sistema de Gestión de Ventas - Cierre de Sesión");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(650, 480);
        setMinimumSize(new Dimension(560, 400));
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {
        boolean esComprador = "COMPRADOR".equals(rol);

        // Encabezado
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 4, 4));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        panelNorte.setBackground(new Color(45, 55, 72));

        JLabel lblTitulo = new JLabel("CIERRE DE SESIÓN / MENÚ FINAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        String sub = esComprador
                ? "Sesión finalizada para comprador: " + (comprador != null ? comprador.getNombre() : "Comprador General")
                : "Sesión finalizada - Panel de Administración";
        JLabel lblSubtitulo = new JLabel(sub, SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(220, 225, 235));

        panelNorte.add(lblTitulo);
        panelNorte.add(lblSubtitulo);

        // Panel Central
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JPanel contenedorResumen = new JPanel(new GridLayout(2, 1, 15, 15));

        JPanel panelDatos = new JPanel(new GridLayout(4, 1, 6, 6));
        panelDatos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resumen de Actividad"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        int totalProductos = contenedor != null ? contenedor.getProductoController().listar().size() : 0;

        if (esComprador) {
            int ordenesComprador = 0;
            if (contenedor != null && comprador != null) {
                List<Pedido> peds = contenedor.getPedidoController().buscar(comprador.getId(), "");
                ordenesComprador = peds.size();
            }
            panelDatos.add(new JLabel("Rol: Comprador"));
            panelDatos.add(new JLabel("Cliente: " + (comprador != null ? comprador.getNombre() : "Comprador General")));
            panelDatos.add(new JLabel("Total de órdenes registradas: " + ordenesComprador));
            panelDatos.add(new JLabel("Productos disponibles en catálogo: " + totalProductos));
        } else {
            int totalClientes = contenedor != null ? contenedor.getClienteController().listar().size() : 0;
            int totalPedidos = contenedor != null ? contenedor.getPedidoController().listar().size() : 0;
            int totalFacturas = contenedor != null ? contenedor.getFacturaController().listar().size() : 0;

            panelDatos.add(new JLabel("Rol: Administrador (Acceso Completo)"));
            panelDatos.add(new JLabel("Clientes en catálogo: " + totalClientes));
            panelDatos.add(new JLabel("Pedidos en el sistema: " + totalPedidos));
            panelDatos.add(new JLabel("Facturas emitidas: " + totalFacturas));
        }

        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 8, 8));
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Opciones de Navegación"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JButton btnVolverInicio = new JButton("Iniciar Sesión con otro Usuario");
        JButton btnVolverSistema = new JButton(esComprador ? "Regresar a Mis Órdenes" : "Regresar al Panel de Administración");
        JButton btnSalirDefinitivo = new JButton("Cerrar Aplicación Completamente");

        btnVolverInicio.addActionListener(e -> volverAInicio());
        btnVolverSistema.addActionListener(e -> volverASistema());
        btnSalirDefinitivo.addActionListener(e -> salirDefinitivo());

        panelAcciones.add(btnVolverInicio);
        panelAcciones.add(btnVolverSistema);
        panelAcciones.add(btnSalirDefinitivo);

        contenedorResumen.add(panelDatos);
        contenedorResumen.add(panelAcciones);
        panelCentro.add(contenedorResumen);

        // Panel Sur
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 12, 10));
        JLabel lblDespedida = new JLabel("Gracias por utilizar el Sistema de Ventas.");
        lblDespedida.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panelSur.add(lblDespedida);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelSur, BorderLayout.SOUTH);
    }

    private void volverAInicio() {
        dispose();
        if (frmInicio != null) {
            frmInicio.reiniciar();
            frmInicio.setVisible(true);
            frmInicio.toFront();
        } else {
            FrmInicio inicio = new FrmInicio(contenedor);
            inicio.setVisible(true);
        }
    }

    private void volverASistema() {
        dispose();
        if (frmPrincipal != null) {
            frmPrincipal.setVisible(true);
            frmPrincipal.toFront();
        } else {
            FrmPrincipal principal = new FrmPrincipal(contenedor, rol, comprador);
            principal.setVisible(true);
        }
    }

    private void salirDefinitivo() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Confirma que desea salir y terminar la aplicación?",
                "Confirmación de Salida",
                JOptionPane.YES_NO_OPTION
        );
        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
