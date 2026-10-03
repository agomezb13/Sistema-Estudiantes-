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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class FrmInicio extends JFrame {

    private static String claveAdmin = "12345";

    private final ContenedorAplicacion contenedor;
    private FrmPrincipal frmPrincipal;

    private final JComboBox<String> cmbTipoAcceso = new JComboBox<>(new String[]{"Invitado", "Administrador"});
    private final JPasswordField txtPassword = new JPasswordField(16);
    private final JLabel lblPassword = new JLabel("Contraseña:");
    private final JLabel lblHint = new JLabel("Acceso libre al catálogo de productos y órdenes.");

    public FrmInicio(ContenedorAplicacion contenedor) {
        this.contenedor = contenedor;
        setTitle("Sistema de Gestión de Ventas - Acceso");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(520, 380);
        setMinimumSize(new Dimension(460, 320));
        setLocationRelativeTo(null);

        // Inicializar conexion a MySQL si esta disponible
        ConexionMySQL.inicializarBaseDatos();

        construirInterfaz();
        configurarEventos();
        actualizarEstadoCampos();
    }

    public static String getClaveAdmin() {
        return claveAdmin;
    }

    public static void setClaveAdmin(String nuevaClave) {
        claveAdmin = nuevaClave;
    }

    private void construirInterfaz() {
        // Encabezado
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 4, 4));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 20));
        panelNorte.setBackground(new Color(30, 41, 59));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE VENTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Seleccione el modo de acceso al sistema", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(148, 163, 184));

        panelNorte.add(lblTitulo);
        panelNorte.add(lblSubtitulo);

        // Formulario central
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 30, 15, 30));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Tipo de Acceso"),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        FormularioHelper.agregarCampo(formCard, 0, "Ingresar como:", cmbTipoAcceso);
        FormularioHelper.agregarCampo(formCard, 1, lblPassword.getText(), txtPassword);

        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblHint.setForeground(new Color(100, 116, 139));
        FormularioHelper.agregarCampo(formCard, 2, "", lblHint);

        panelCentro.add(formCard);

        // Botones inferiores
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnSalir = new JButton("Salir");

        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnIngresar.setPreferredSize(new Dimension(130, 36));
        btnSalir.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnSalir.setPreferredSize(new Dimension(100, 36));

        btnIngresar.addActionListener(e -> intentarLogin());
        btnSalir.addActionListener(e -> System.exit(0));

        getRootPane().setDefaultButton(btnIngresar);

        panelSur.add(btnIngresar);
        panelSur.add(btnSalir);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelSur, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        cmbTipoAcceso.addActionListener(e -> actualizarEstadoCampos());
    }

    private void actualizarEstadoCampos() {
        String opcion = (String) cmbTipoAcceso.getSelectedItem();
        boolean esAdmin = "Administrador".equals(opcion);

        if (esAdmin) {
            txtPassword.setEnabled(true);
            txtPassword.setText("");
            lblPassword.setText("Contraseña:");
            lblHint.setText("Ingrese la contraseña de Administrador.");
            txtPassword.requestFocus();
        } else {
            txtPassword.setEnabled(false);
            txtPassword.setText("");
            lblPassword.setText("Contraseña (No requerida):");
            lblHint.setText("Acceso libre al catálogo de productos y órdenes.");
        }
    }

    private void intentarLogin() {
        String opcion = (String) cmbTipoAcceso.getSelectedItem();
        boolean esAdmin = "Administrador".equals(opcion);

        if (esAdmin) {
            String pass = new String(txtPassword.getPassword()).trim();
            if (!claveAdmin.equals(pass)) {
                JOptionPane.showMessageDialog(this,
                        "Contraseña incorrecta para Administrador.\nPor favor intente nuevamente.",
                        "Acceso Denegado",
                        JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }
            abrirPrincipal("ADMIN");
        } else {
            abrirPrincipal("INVITADO");
        }
    }

    private void abrirPrincipal(String rol) {
        setVisible(false);
        frmPrincipal = new FrmPrincipal(contenedor, rol, null);
        frmPrincipal.setFrmInicio(this);
        frmPrincipal.setVisible(true);
        frmPrincipal.toFront();
    }

    public void reiniciar() {
        txtPassword.setText("");
        cmbTipoAcceso.setSelectedIndex(0);
        actualizarEstadoCampos();
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
