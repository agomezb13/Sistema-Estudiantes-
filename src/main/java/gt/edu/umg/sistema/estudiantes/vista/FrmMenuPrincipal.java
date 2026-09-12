package gt.edu.umg.sistema.estudiantes.vista;

import javax.swing.JFrame;

public class FrmMenuPrincipal extends javax.swing.JFrame {

    public FrmMenuPrincipal() {
        initComponents();
        // Centrar la ventana
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        btnEstudiantes = new javax.swing.JButton();
        btnFacturacion = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión");

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MENÚ PRINCIPAL");

        btnEstudiantes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEstudiantes.setText("Gestión de Estudiantes");
        btnEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudiantesActionPerformed(evt);
            }
        });

        btnFacturacion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnFacturacion.setText("Módulo de Facturación");
        btnFacturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturacionActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir del Sistema");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnFacturacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEstudiantes, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                                        .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap(79, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir)
                                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(lblTitulo)
                                .addGap(42, 42, 42)
                                .addComponent(btnEstudiantes, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnFacturacion, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addComponent(btnSalir)
                                .addGap(18, 18, 18))
        );

        pack();
    }

    private void btnEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {
        // Abre el formulario de Estudiantes
        FrmEstudiante frmEst = new FrmEstudiante();
        frmEst.setLocationRelativeTo(null);
        // Opcional: Para que no se cierre todo el sistema al cerrar el form de estudiantes
        frmEst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmEst.setVisible(true);
    }

    private void btnFacturacionActionPerformed(java.awt.event.ActionEvent evt) {
        // Abre el formulario de Facturación
        FrmFactura frmFact = new FrmFactura();
        frmFact.setLocationRelativeTo(null);
        // Opcional: Para que no se cierre todo el sistema al cerrar el form de facturas
        frmFact.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmFact.setVisible(true);
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private javax.swing.JButton btnEstudiantes;
    private javax.swing.JButton btnFacturacion;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblTitulo;
}