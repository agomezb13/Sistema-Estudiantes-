package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import javax.swing.JOptionPane;

public class FrmInicio extends javax.swing.JFrame {

    private static String claveAdmin = "12345";
    private FrmPrincipal frmPrincipal;

    public FrmInicio() {
        initComponents();
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnIngresar);
        ConexionMySQL.inicializarBaseDatos();
        actualizarEstadoCampos();
    }

    public static String getClaveAdmin() {
        return claveAdmin;
    }

    public static void setClaveAdmin(String nuevaClave) {
        claveAdmin = nuevaClave;
    }

    private void actualizarEstadoCampos() {
        String opcion = (String) cmbTipoAcceso.getSelectedItem();
        boolean esAdmin = "Administrador".equals(opcion);

        if (esAdmin) {
            txtPassword.setEnabled(true);
            txtPassword.setText("");
            lblPassword.setText("Contraseña:");
            lblHint.setText("");
            txtPassword.requestFocus();
        } else {
            txtPassword.setEnabled(false);
            txtPassword.setText("");
            lblHint.setText("");
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
        frmPrincipal = new FrmPrincipal(rol, null);
        frmPrincipal.setFrmInicio(this);
        frmPrincipal.setVisible(true);
        frmPrincipal.toFront();
    }

    public void reiniciar() {
        txtPassword.setText("");
        cmbTipoAcceso.setSelectedIndex(0);
        actualizarEstadoCampos();
    }

    private void cmbTipoAccesoActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarEstadoCampos();
    }

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        intentarLogin();
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNorte = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblTipoUsuario = new javax.swing.JLabel();
        cmbTipoAcceso = new javax.swing.JComboBox<>();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblHint = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión de Ventas - Acceso");
        setResizable(false);

        panelNorte.setBackground(new java.awt.Color(0, 255, 204));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Empresa");

        javax.swing.GroupLayout panelNorteLayout = new javax.swing.GroupLayout(panelNorte);
        panelNorte.setLayout(panelNorteLayout);
        panelNorteLayout.setHorizontalGroup(
            panelNorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNorteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelNorteLayout.setVerticalGroup(
            panelNorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNorteLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblTitulo)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        lblTipoUsuario.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTipoUsuario.setText("Ingresar como:");

        cmbTipoAcceso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invitado", "Administrador" }));
        cmbTipoAcceso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoAccesoActionPerformed(evt);
            }
        });

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPassword.setText("Contraseña:");

        txtPassword.setEnabled(false);

        lblHint.setFont(new java.awt.Font("Segoe UI", 2, 11)); // NOI18N
        lblHint.setForeground(new java.awt.Color(100, 116, 139));

        btnIngresar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIngresar.setText("Ingresar");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelNorte, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHint, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTipoUsuario)
                            .addComponent(lblPassword))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTipoAcceso, 0, 240, Short.MAX_VALUE)
                            .addComponent(txtPassword))))
                .addContainerGap(58, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelNorte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTipoUsuario)
                    .addComponent(cmbTipoAcceso, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHint)
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        java.awt.EventQueue.invokeLater(() -> new FrmInicio().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbTipoAcceso;
    private javax.swing.JLabel lblHint;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTipoUsuario;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelNorte;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
