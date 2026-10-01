package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
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

public class FrmInicio extends JFrame {

    private final ContenedorAplicacion contenedor;
    private FrmPrincipal frmPrincipal;
    private final JLabel lblEstadoBd = new JLabel();

    public FrmInicio(ContenedorAplicacion contenedor) {
        this.contenedor = contenedor;
        setTitle("Sistema de Gestión de Ventas - Menú de Inicio");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 520);
        setMinimumSize(new Dimension(600, 440));
        setLocationRelativeTo(null);

        construirInterfaz();
        actualizarEstadoBd();
    }

    private void construirInterfaz() {
        // Encabezado
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 4, 4));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        panelNorte.setBackground(new Color(33, 50, 75));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE VENTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Arquitectura Multicapa & Modelo Relacional de Ventas", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(200, 215, 235));

        panelNorte.add(lblTitulo);
        panelNorte.add(lblSubtitulo);

        // Panel Central
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel tarjetaBd = new JPanel(new BorderLayout(10, 10));
        tarjetaBd.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Estado del Acceso a Datos"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        lblEstadoBd.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tarjetaBd.add(lblEstadoBd, BorderLayout.CENTER);

        JPanel panelBotonesBd = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnProbarBd = new JButton("Probar Conexión MySQL");
        JButton btnConfigurarBd = new JButton("Configurar BD");
        btnProbarBd.addActionListener(e -> probarConexion());
        btnConfigurarBd.addActionListener(e -> configurarConexion());
        panelBotonesBd.add(btnProbarBd);
        panelBotonesBd.add(btnConfigurarBd);
        tarjetaBd.add(panelBotonesBd, BorderLayout.SOUTH);

        JPanel tarjetaAcciones = new JPanel(new GridLayout(2, 1, 10, 10));
        tarjetaAcciones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Opciones de Inicio"),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JButton btnEntrar = new JButton("INGRESAR AL SISTEMA DE VENTAS");
        btnEntrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnEntrar.setPreferredSize(new Dimension(320, 45));
        btnEntrar.addActionListener(e -> ingresarSistema());

        JButton btnMenuFinal = new JButton("Ir al Menú Final / Cierre de Sesión");
        btnMenuFinal.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnMenuFinal.addActionListener(e -> abrirMenuFinal());

        tarjetaAcciones.add(btnEntrar);
        tarjetaAcciones.add(btnMenuFinal);

        JPanel contenedorCentral = new JPanel(new GridLayout(2, 1, 15, 15));
        contenedorCentral.add(tarjetaBd);
        contenedorCentral.add(tarjetaAcciones);

        panelCentro.add(contenedorCentral);

        // Panel Sur (Pie)
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        JLabel lblPie = new JLabel("Universidad Mariano Gálvez de Guatemala - Ingeniería en Sistemas");
        lblPie.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblPie.setForeground(Color.GRAY);
        panelSur.add(lblPie);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelSur, BorderLayout.SOUTH);
    }

    private void actualizarEstadoBd() {
        boolean conexionOk = ConexionMySQL.probarConexion();
        if (conexionOk) {
            lblEstadoBd.setText("<html><font color='green'><b>Conexión a MySQL Exitosa.</b></font><br>"
                    + "Base de datos disponible para operaciones persistentes.</html>");
        } else {
            lblEstadoBd.setText("<html><font color='#B8860B'><b>Modo de Almacenamiento en Memoria Activo.</b></font><br>"
                    + "MySQL no está conectado actualmente. El sistema funciona con datos locales en memoria.</html>");
        }
    }

    private void probarConexion() {
        boolean ok = ConexionMySQL.probarConexion();
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "La conexión a la base de datos MySQL se estableció correctamente.",
                    "Conexión Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo conectar al servidor MySQL.\n"
                    + "Verifique que el servicio de MySQL (puerto 3306) esté iniciado y que la base 'sistema_ventas' exista.\n"
                    + "El sistema continuará operando en modo memoria sin interrupciones.",
                    "Aviso de Conexión",
                    JOptionPane.WARNING_MESSAGE);
        }
        actualizarEstadoBd();
    }

    private void configurarConexion() {
        String host = JOptionPane.showInputDialog(this, "Host del servidor MySQL:", "localhost");
        if (host == null || host.trim().isEmpty()) return;

        String puertoStr = JOptionPane.showInputDialog(this, "Puerto:", "3306");
        if (puertoStr == null || puertoStr.trim().isEmpty()) return;
        int puerto = 3306;
        try {
            puerto = Integer.parseInt(puertoStr.trim());
        } catch (NumberFormatException ignored) {}

        String base = JOptionPane.showInputDialog(this, "Nombre de la base de datos:", "sistema_ventas");
        if (base == null || base.trim().isEmpty()) return;

        String usuario = JOptionPane.showInputDialog(this, "Usuario:", "root");
        if (usuario == null) return;

        String clave = JOptionPane.showInputDialog(this, "Contraseña:", "");
        if (clave == null) clave = "";

        ConexionMySQL.configurar(host.trim(), puertoStr.trim(), base.trim(), usuario.trim(), clave);
        probarConexion();
    }

    private void ingresarSistema() {
        setVisible(false);
        if (frmPrincipal == null) {
            frmPrincipal = new FrmPrincipal(contenedor);
            frmPrincipal.setFrmInicio(this);
        }
        frmPrincipal.setVisible(true);
        frmPrincipal.toFront();
    }

    private void abrirMenuFinal() {
        setVisible(false);
        FrmFinal frmFinal = new FrmFinal(contenedor, frmPrincipal, this);
        frmFinal.setVisible(true);
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
        java.awt.EventQueue.invokeLater(() -> new FrmInicio(new ContenedorAplicacion()).setVisible(true));
    }
}
