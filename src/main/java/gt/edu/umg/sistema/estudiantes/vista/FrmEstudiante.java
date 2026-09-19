package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.EstudianteController;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmEstudiante extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmEstudiante.class.getName());

    EstudianteController controller;

    public FrmEstudiante() {
        initComponents();
        controller = new EstudianteController();
        crearMenuSuperior();
        refrescarTabla();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void crearMenuSuperior() {
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();

        javax.swing.JMenu menuNavegacion = new javax.swing.JMenu("Menú");
        javax.swing.JMenuItem itemMenuPrincipal = new javax.swing.JMenuItem("Menú Principal");
        javax.swing.JMenuItem itemFacturacion = new javax.swing.JMenuItem("Módulo de Facturación");
        javax.swing.JMenuItem itemSalir = new javax.swing.JMenuItem("Salir");

        itemMenuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FrmMenuPrincipal menu = new FrmMenuPrincipal();
                menu.setVisible(true);
                dispose();
            }
        });

        itemFacturacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FrmFactura frmFact = new FrmFactura();
                frmFact.setVisible(true);
                dispose();
            }
        });

        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });

        menuNavegacion.add(itemMenuPrincipal);
        menuNavegacion.add(itemFacturacion);
        menuNavegacion.addSeparator();
        menuNavegacion.add(itemSalir);

        javax.swing.JMenu menuAcciones = new javax.swing.JMenu("Acciones de Estudiante");
        javax.swing.JMenuItem itemGrabar = new javax.swing.JMenuItem("Grabar Estudiante");
        javax.swing.JMenuItem itemBuscar = new javax.swing.JMenuItem("Buscar Estudiante por ID");
        javax.swing.JMenuItem itemEliminar = new javax.swing.JMenuItem("Eliminar Estudiante");
        javax.swing.JMenuItem itemLimpiar = new javax.swing.JMenuItem("Limpiar Formulario");
        javax.swing.JMenuItem itemRefrescar = new javax.swing.JMenuItem("Actualizar Tabla");

        itemGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grabarEstudiante();
            }
        });

        itemBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarEstudiante();
            }
        });

        itemEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarEstudiante();
            }
        });

        itemLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                limpiarCampos();
            }
        });

        itemRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refrescarTabla();
            }
        });

        menuAcciones.add(itemGrabar);
        menuAcciones.add(itemBuscar);
        menuAcciones.add(itemEliminar);
        menuAcciones.addSeparator();
        menuAcciones.add(itemLimpiar);
        menuAcciones.add(itemRefrescar);

        menuBar.add(menuNavegacion);
        menuBar.add(menuAcciones);
        this.setJMenuBar(menuBar);
    }

    public void cargarDatosTabla(List<Estudiante> listaEstudiantes) {
        String[] columnas = {"ID", "Nombres", "Apellidos", "Carnet", "Correo"};

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Estudiante est : listaEstudiantes) {
            Object[] fila = {
                est.getId(),
                est.getNombres(),
                est.getApellidos(),
                est.getCarnet(),
                est.getEmail()
            };
            modelo.addRow(fila);
        }

        jTable1.setModel(modelo);
    }

    private void grabarEstudiante() {
        try {
            Estudiante estudiante = new Estudiante();
            estudiante.setId(Integer.parseInt(txtId.getText()));
            estudiante.setNombres(txtNombres.getText());
            estudiante.setApellidos(txtApellidos.getText());
            estudiante.setCarnet(txtCarnet.getText());
            estudiante.setEmail(txtEmail.getText());

            controller.Guardar(estudiante);

            JOptionPane.showMessageDialog(this, "Estudiante grabado con éxito.");

            limpiarCampos();
            refrescarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El ID debe ser un número válido.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEstudiante() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante a buscar:", "Buscar Estudiante", JOptionPane.QUESTION_MESSAGE);

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idBuscado = Integer.parseInt(idStr);
                boolean encontrado = false;

                for (Estudiante est : controller.GetEstudiantes()) {
                    if (est.getId() == idBuscado) {
                        txtId.setText(String.valueOf(est.getId()));
                        txtNombres.setText(est.getNombres());
                        txtApellidos.setText(est.getApellidos());
                        txtEmail.setText(est.getEmail());
                        txtCarnet.setText(est.getCarnet());
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún estudiante con el ID: " + idBuscado, "No encontrado", JOptionPane.WARNING_MESSAGE);
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: El ID debe ser un número entero válido.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEstudiante() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante que desea eliminar:", "Eliminar Estudiante", JOptionPane.WARNING_MESSAGE);

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idAEliminar = Integer.parseInt(idStr);

                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar al estudiante con ID " + idAEliminar + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    controller.Eliminar(idAEliminar);
                    JOptionPane.showMessageDialog(this, "Estudiante eliminado correctamente.");
                    refrescarTabla();
                    limpiarCampos();
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: El ID debe ser un número entero válido.", "Entrada Inválida", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtCarnet.setText("");
    }

    private void refrescarTabla() {
        try {
            cargarDatosTabla(controller.GetEstudiantes());
        } catch (Exception e) {
            logger.severe("No se pudo cargar la lista de estudiantes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblnombres = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        lblnombres1 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        lblnombres2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblnombres3 = new javax.swing.JLabel();
        txtCarnet = new javax.swing.JTextField();
        BtnGrabar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestión de Estudiantes");
        setMinimumSize(new java.awt.Dimension(520, 600));

        jLabel1.setText("Id");

        lblnombres.setText("Nombres");

        lblnombres1.setText("Apellidos");

        lblnombres2.setText("email");

        lblnombres3.setText("carnet");

        BtnGrabar.setText("Grabar");
        BtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGrabarActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombres", "Apellidos", "Carnet", "Correo"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(BtnGrabar)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblnombres3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtCarnet))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblnombres2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtEmail))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblnombres1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtApellidos))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(213, 213, 213))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(lblnombres, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombres)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombres1)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombres2)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombres3)
                    .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(BtnGrabar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addGap(23, 23, 23))
        );

        pack();
    }

    private void BtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {
        grabarEstudiante();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEstudiante().setVisible(true);
            }
        });
    }

    private javax.swing.JButton BtnGrabar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    private javax.swing.JLabel lblnombres;
    private javax.swing.JLabel lblnombres1;
    private javax.swing.JLabel lblnombres2;
    private javax.swing.JLabel lblnombres3;
    public javax.swing.JTextField txtApellidos;
    public javax.swing.JTextField txtCarnet;
    public javax.swing.JTextField txtEmail;
    public javax.swing.JTextField txtId;
    public javax.swing.JTextField txtNombres;
}
