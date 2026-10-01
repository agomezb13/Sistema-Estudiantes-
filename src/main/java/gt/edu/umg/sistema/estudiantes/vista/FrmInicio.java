package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.config.ContenedorAplicacion;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
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

    private final JComboBox<String> cmbRol = new JComboBox<>(new String[]{"Administrador", "Comprador"});
    private final JComboBox<Cliente> cmbComprador = new JComboBox<>();
    private final JButton btnNuevoComprador = new JButton("+ Registrar Comprador");
    private final JPasswordField txtPassword = new JPasswordField(16);
    private final JLabel lblPassword = new JLabel("Contraseña:");
    private final JLabel lblHint = new JLabel("Ingrese la contraseña de Administrador.");

    public FrmInicio(ContenedorAplicacion contenedor) {
        this.contenedor = contenedor;
        setTitle("Sistema de Gestión de Ventas - Inicio de Sesión");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(580, 460);
        setMinimumSize(new Dimension(520, 400));
        setLocationRelativeTo(null);

        cargarClientesComprador();
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

    public void cargarClientesComprador() {
        cmbComprador.removeAllItems();
        List<Cliente> clientes = contenedor.getClienteController().listar();
        for (Cliente c : clientes) {
            cmbComprador.addItem(c);
        }
        if (cmbComprador.getItemCount() == 0) {
            Cliente general = new Cliente(1, "Cliente General", "cliente@tienda.com", "0000-0000", "Ciudad", "0000000000000", "CF", "ACTIVO");
            cmbComprador.addItem(general);
        }
    }

    private void construirInterfaz() {
        // Encabezado
        JPanel panelNorte = new JPanel(new GridLayout(2, 1, 4, 4));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 20));
        panelNorte.setBackground(new Color(30, 41, 59));

        JLabel lblTitulo = new JLabel("SISTEMA DE GESTIÓN DE VENTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Inicio de Sesión y Control de Acceso", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(148, 163, 184));

        panelNorte.add(lblTitulo);
        panelNorte.add(lblSubtitulo);

        // Panel Central: Formulario de Login
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Credenciales de Acceso"),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        FormularioHelper.agregarCampo(formCard, 0, "Tipo de Usuario:", cmbRol);

        JPanel panelCompradorFila = new JPanel(new BorderLayout(6, 0));
        panelCompradorFila.add(cmbComprador, BorderLayout.CENTER);
        panelCompradorFila.add(btnNuevoComprador, BorderLayout.EAST);
        FormularioHelper.agregarCampo(formCard, 1, "Comprador:", panelCompradorFila);

        FormularioHelper.agregarCampo(formCard, 2, "Contraseña:", txtPassword);

        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblHint.setForeground(new Color(100, 116, 139));
        FormularioHelper.agregarCampo(formCard, 3, "", lblHint);

        panelCentro.add(formCard);

        // Panel Sur: Botones
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnIngresar = new JButton("Iniciar Sesión");
        JButton btnSalir = new JButton("Salir");

        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnIngresar.setPreferredSize(new Dimension(140, 36));
        btnSalir.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnSalir.setPreferredSize(new Dimension(100, 36));

        btnIngresar.addActionListener(e -> intentarLogin());
        btnSalir.addActionListener(e -> System.exit(0));
        btnNuevoComprador.addActionListener(e -> abrirRegistroComprador());

        getRootPane().setDefaultButton(btnIngresar);

        panelSur.add(btnIngresar);
        panelSur.add(btnSalir);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelSur, BorderLayout.SOUTH);
    }

    private void configurarEventos() {
        cmbRol.addActionListener(e -> actualizarEstadoCampos());
    }

    private void actualizarEstadoCampos() {
        String rol = (String) cmbRol.getSelectedItem();
        boolean esAdmin = "Administrador".equals(rol);

        if (esAdmin) {
            cmbComprador.setEnabled(false);
            btnNuevoComprador.setEnabled(false);
            txtPassword.setEnabled(true);
            lblPassword.setText("Contraseña:");
            lblHint.setText("Ingrese la contraseña de Administrador.");
            txtPassword.requestFocus();
        } else {
            cmbComprador.setEnabled(true);
            btnNuevoComprador.setEnabled(true);
            txtPassword.setEnabled(false);
            txtPassword.setText("");
            lblPassword.setText("Contraseña (No requerida):");
            lblHint.setText("Acceso para compradores. Puede seleccionar o registrar un comprador.");
            cmbComprador.requestFocus();
        }
    }

    private void abrirRegistroComprador() {
        DlgRegistroComprador dlg = new DlgRegistroComprador(this, contenedor.getClienteController(), nuevo -> {
            cargarClientesComprador();
            for (int i = 0; i < cmbComprador.getItemCount(); i++) {
                Cliente c = cmbComprador.getItemAt(i);
                if (c != null && c.getId() == nuevo.getId()) {
                    cmbComprador.setSelectedIndex(i);
                    break;
                }
            }
        });
        dlg.setVisible(true);
    }

    private void intentarLogin() {
        String rol = (String) cmbRol.getSelectedItem();
        boolean esAdmin = "Administrador".equals(rol);

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

            abrirPrincipal("ADMIN", null);
        } else {
            Cliente comprador = (Cliente) cmbComprador.getSelectedItem();
            if (comprador == null) {
                JOptionPane.showMessageDialog(this,
                        "Por favor registre o seleccione un comprador para ingresar.",
                        "Comprador Requerido",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            abrirPrincipal("COMPRADOR", comprador);
        }
    }

    private void abrirPrincipal(String rol, Cliente comprador) {
        setVisible(false);
        frmPrincipal = new FrmPrincipal(contenedor, rol, comprador);
        frmPrincipal.setFrmInicio(this);
        frmPrincipal.setVisible(true);
        frmPrincipal.toFront();
    }

    public void reiniciar() {
        txtPassword.setText("");
        cargarClientesComprador();
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
