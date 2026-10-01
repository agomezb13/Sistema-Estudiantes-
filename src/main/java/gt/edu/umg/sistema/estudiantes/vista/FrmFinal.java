package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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

    public FrmFinal(ContenedorAplicacion contenedor, FrmPrincipal frmPrincipal, FrmInicio frmInicio) {
        this.contenedor = contenedor;
        this.frmPrincipal = frmPrincipal;
        this.frmInicio = frmInicio;

        setTitle("Sistema de Gestión de Ventas - Menú Final");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(650, 480);
        setMinimumSize(new Dimension(560, 400));
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {
        // Encabezado
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 4, 4));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        panelNorte.setBackground(new Color(45, 55, 72));

        JLabel lblTitulo = new JLabel("CIERRE DE SESIÓN / MENÚ FINAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Resumen de actividad y opciones de salida del sistema", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(220, 225, 235));

        panelNorte.add(lblTitulo);
        panelNorte.add(lblSubtitulo);

        // Panel Central: Resumen y opciones
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JPanel contenedorResumen = new JPanel(new GridLayout(2, 1, 15, 15));

        JPanel panelDatos = new JPanel(new GridLayout(4, 1, 6, 6));
        panelDatos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Resumen de la Sesión"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        int totalClientes = contenedor != null ? contenedor.getClienteController().listar().size() : 0;
        int totalProductos = contenedor != null ? contenedor.getProductoController().listar().size() : 0;
        int totalPedidos = contenedor != null ? contenedor.getPedidoController().listar().size() : 0;
        int totalFacturas = contenedor != null ? contenedor.getFacturaController().listar().size() : 0;

        panelDatos.add(new JLabel("Clientes en catálogo: " + totalClientes));
        panelDatos.add(new JLabel("Productos registrados: " + totalProductos));
        panelDatos.add(new JLabel("Pedidos en el sistema: " + totalPedidos));
        panelDatos.add(new JLabel("Facturas emitidas: " + totalFacturas));

        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 8, 8));
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Opciones Disponibles"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JButton btnVolverInicio = new JButton("Volver al Menú de Inicio");
        JButton btnVolverSistema = new JButton("Regresar al Sistema de Ventas");
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
            FrmPrincipal principal = new FrmPrincipal(contenedor);
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
