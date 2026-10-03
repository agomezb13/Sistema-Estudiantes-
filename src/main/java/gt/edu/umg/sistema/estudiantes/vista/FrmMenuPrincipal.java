package gt.edu.umg.sistema.estudiantes.vista;

import javax.swing.JFrame;

public class FrmMenuPrincipal extends javax.swing.JFrame {

    public FrmMenuPrincipal() {
        initComponents();
        crearMenuSuperior();
        this.setLocationRelativeTo(null);
    }

    private void crearMenuSuperior() {
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();

        javax.swing.JMenu menuModulos = new javax.swing.JMenu("Módulos");
        javax.swing.JMenuItem itemEstudiantes = new javax.swing.JMenuItem("Gestión de Estudiantes");
        javax.swing.JMenuItem itemFacturacion = new javax.swing.JMenuItem("Módulo de Facturación");
        javax.swing.JMenuItem itemSalir = new javax.swing.JMenuItem("Salir");

        itemEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudiantesActionPerformed(evt);
            }
        });

        itemFacturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturacionActionPerformed(evt);
            }
        });

        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        menuModulos.add(itemEstudiantes);
        menuModulos.add(itemFacturacion);
        menuModulos.addSeparator();
        menuModulos.add(itemSalir);

        menuBar.add(menuModulos);
        this.setJMenuBar(menuBar);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gbc;

        lblTitulo = new javax.swing.JLabel();
        btnEstudiantes = new javax.swing.JButton();
        btnFacturacion = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión");
        setMinimumSize(new java.awt.Dimension(420, 360));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("MENÚ PRINCIPAL");
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(15, 30, 25, 30);
        getContentPane().add(lblTitulo, gbc);

        btnEstudiantes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEstudiantes.setText("Gestión de Estudiantes");
        btnEstudiantes.setPreferredSize(new java.awt.Dimension(260, 50));
        btnEstudiantes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstudiantesActionPerformed(evt);
            }
        });
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(10, 30, 10, 30);
        getContentPane().add(btnEstudiantes, gbc);

        btnFacturacion.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnFacturacion.setText("Módulo de Facturación");
        btnFacturacion.setPreferredSize(new java.awt.Dimension(260, 50));
        btnFacturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturacionActionPerformed(evt);
            }
        });
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(10, 30, 15, 30);
        getContentPane().add(btnFacturacion, gbc);

        btnSalir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSalir.setText("Salir del Sistema");
        btnSalir.setPreferredSize(new java.awt.Dimension(260, 40));
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(15, 30, 15, 30);
        getContentPane().add(btnSalir, gbc);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEstudiantesActionPerformed(java.awt.event.ActionEvent evt) {
        FrmEstudiante frmEst = new FrmEstudiante();
        frmEst.setLocationRelativeTo(null);
        frmEst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmEst.setVisible(true);
    }

    private void btnFacturacionActionPerformed(java.awt.event.ActionEvent evt) {
        FrmFactura frmFact = new FrmFactura();
        frmFact.setLocationRelativeTo(null);
        frmFact.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmFact.setVisible(true);
    }

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEstudiantes;
    private javax.swing.JButton btnFacturacion;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblTitulo;
    // End of variables declaration//GEN-END:variables
}