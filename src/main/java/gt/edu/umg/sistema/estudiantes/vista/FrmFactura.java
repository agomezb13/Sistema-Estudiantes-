package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;

public class FrmFactura extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmFactura.class.getName());

    private Factura facturaActual;

    public FrmFactura() {
        initComponents();
        facturaActual = new Factura();
        crearMenuSuperior();
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void crearMenuSuperior() {
        javax.swing.JMenuBar menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu menuAcciones = new javax.swing.JMenu("Acciones de Factura");

        javax.swing.JMenuItem itemGuardar = new javax.swing.JMenuItem("Guardar Factura");
        javax.swing.JMenuItem itemImprimir = new javax.swing.JMenuItem("Imprimir Factura"); // NUEVO
        javax.swing.JMenuItem itemBuscar = new javax.swing.JMenuItem("Buscar Cliente/Factura");
        javax.swing.JMenuItem itemLimpiar = new javax.swing.JMenuItem("Limpiar Formulario");
        javax.swing.JMenuItem itemEliminarFila = new javax.swing.JMenuItem("Eliminar Fila Seleccionada (Tabla)");
        javax.swing.JMenuItem itemEliminarBD = new javax.swing.JMenuItem("Eliminar Factura (Base de Datos)");

        itemGuardar.addActionListener(evt -> guardarFactura());
        itemImprimir.addActionListener(evt -> imprimirFactura()); // NUEVO
        itemBuscar.addActionListener(evt -> buscarCliente());
        itemLimpiar.addActionListener(evt -> limpiarFormulario());
        itemEliminarFila.addActionListener(evt -> eliminarFila());
        itemEliminarBD.addActionListener(evt -> eliminarFacturaBD());

        menuAcciones.add(itemGuardar);
        menuAcciones.add(itemImprimir); // NUEVO
        menuAcciones.add(itemBuscar);
        menuAcciones.add(itemEliminarBD);
        menuAcciones.addSeparator();
        menuAcciones.add(itemEliminarFila);
        menuAcciones.add(itemLimpiar);

        menuBar.add(menuAcciones);
        this.setJMenuBar(menuBar);
    }

    private void guardarFactura() {
        try {
            FacturaDAO dao = new FacturaDAOImpl();
            String nit = txtNitReceptor.getText();
            String nombre = txtNombreCliente.getText();
            String direccion = txtDirecciónCliente.getText();
            String fechaEmision = txtFechaHoraEmision.getText();
            String fechaCertificacion = txtFechaHoraCertificacion.getText();

            double subtotal = Double.parseDouble(lblSubtotal.getText().replace(",", "."));
            double iva = Double.parseDouble(lblIVA.getText().replace(",", "."));
            double total = Double.parseDouble(lblTotal.getText().replace(",", "."));

            dao.guardarFactura(nit, nombre, direccion, fechaEmision, fechaCertificacion, subtotal, iva, total);
            javax.swing.JOptionPane.showMessageDialog(this, "¡Factura guardada con éxito!");
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: Asegúrese de agregar productos y calcular totales antes de guardar.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void imprimirFactura() {
        // Aquí puedes programar la lógica para imprimir o generar un PDF en el futuro
        javax.swing.JOptionPane.showMessageDialog(this, "Generando vista previa de impresión para la factura actual...");
    }

    private void eliminarFacturaBD() {
        String nitStr = javax.swing.JOptionPane.showInputDialog(this, "Ingrese el NIT de la factura que desea eliminar de la Base de Datos:", "Eliminar Factura", javax.swing.JOptionPane.WARNING_MESSAGE);

        if (nitStr != null && !nitStr.trim().isEmpty()) {
            int confirmacion = javax.swing.JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar las facturas registradas con el NIT " + nitStr + "?", "Confirmar Eliminación", javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
                FacturaDAO dao = new FacturaDAOImpl();
                dao.eliminarFactura(nitStr);
                javax.swing.JOptionPane.showMessageDialog(this, "Proceso de eliminación completado. Revise la base de datos para confirmar.");
                limpiarFormulario();
            }
        }
    }

    private void limpiarFormulario() {
        txtNitReceptor.setText("");
        txtNombreCliente.setText("");
        txtDirecciónCliente.setText("");
        txtFechaHoraEmision.setText("");
        txtFechaHoraCertificacion.setText("");

        txtCantidad.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        txtDescuento.setText("");

        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) tblDetalleFactura.getModel();
        modelo.setRowCount(0);

        lblSubtotal.setText("0.00");
        lblIVA.setText("0.00");
        lblTotal.setText("0.00");

        facturaActual = new Factura();
    }

    private void eliminarFila() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) tblDetalleFactura.getModel();
        int filaSeleccionada = tblDetalleFactura.getSelectedRow();

        if (filaSeleccionada >= 0) {
            if (filaSeleccionada < facturaActual.getDetalles().size()) {
                facturaActual.getDetalles().remove(filaSeleccionada);
            }

            modelo.removeRow(filaSeleccionada);
            recalcularTotales(modelo);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila de la tabla para eliminar.");
        }
    }

    private void buscarCliente() {
        String nitBuscado = javax.swing.JOptionPane.showInputDialog(this, "Ingrese el NIT a buscar:", "Buscar Cliente", javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (nitBuscado != null && !nitBuscado.trim().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Simulando búsqueda de datos para el NIT: " + nitBuscado);
        }
    }

    private void recalcularTotales(javax.swing.table.DefaultTableModel modelo) {
        double subtotal = 0;
        for(int i = 0; i < modelo.getRowCount(); i++){
            subtotal += Double.parseDouble(modelo.getValueAt(i, 6).toString());
        }

        double iva = subtotal * 0.12;
        double totalFactura = subtotal + iva;
        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblIVA.setText(String.format("%.2f", iva));
        lblTotal.setText(String.format("%.2f", totalFactura));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        txtNitReceptor = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDirecciónCliente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtFechaHoraEmision = new javax.swing.JTextField();
        txtFechaHoraCertificacion = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetalleFactura = new javax.swing.JTable();
        lblSubtotal = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        lblIVA = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        BttnAgregar = new javax.swing.JButton();
        txtCantidad = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtDescuento = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("FACTURACIÓN ");

        jLabel2.setText("Angel Arturo Gómez Brán");

        jLabel3.setText("Nit Emisor:");

        jLabel4.setText("12 calle 4-10 zona 18, Zona Portales, Guatemala, Guatemala ");

        jLabel5.setText("Teléfono:");

        jLabel6.setText("NÚMERO DE AUTORIZACION:");

        jLabel7.setText("12345678");

        jLabel8.setText("Serie:");

        jLabel9.setText("No. Factura");

        jLabel10.setText("11223344");

        jLabel11.setText("55664455");

        jLabel12.setText("Nit Receptor:");

        txtNitReceptor.addActionListener(this::txtNitReceptorActionPerformed);

        jLabel13.setText("Nombre:");

        txtNombreCliente.addActionListener(this::txtNombreClienteActionPerformed);

        jLabel14.setText("Dirección:");

        jLabel15.setText("Fecha y hora de emisión:");

        jLabel16.setText("Fecha y hoa de certificación:");

        txtFechaHoraEmision.addActionListener(this::txtFechaHoraEmisionActionPerformed);

        tblDetalleFactura.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "No. ", "B/S", "Cantidad", "Descripción ", "Precio unitario con IVA", "Descuento ", "Total"
                }
        ));
        jScrollPane2.setViewportView(tblDetalleFactura);

        lblSubtotal.setText("0.00");

        jLabel25.setText("Subtotal");

        lblIVA.setText("0.00");

        jLabel27.setText("IVA");

        lblTotal.setText("0.00");

        jLabel29.setText("Total");

        BttnAgregar.setText("Agregar");
        BttnAgregar.addActionListener(this::BttnAgregarActionPerformed);

        txtDescuento.addActionListener(this::txtDescuentoActionPerformed);

        jLabel17.setText("Cantidad");
        jLabel17.setMinimumSize(new java.awt.Dimension(48, 14));

        jLabel18.setText("Descripción ");

        jLabel19.setText("Precio");

        jLabel20.setText("Descuento");

        jLabel21.setText("A-001");

        jLabel22.setText("000001");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel1)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel3)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jLabel10))
                                                                        .addComponent(jLabel4)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel5)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(jLabel11)))
                                                                .addGap(243, 243, 243)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel9)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(jLabel22))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel8)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(jLabel21))
                                                                        .addComponent(jLabel7)
                                                                        .addComponent(jLabel6)))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(34, 34, 34)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel12)
                                                        .addComponent(jLabel13)
                                                        .addComponent(jLabel14))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtNitReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jLabel15)
                                                                                .addGap(26, 26, 26))
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel16)
                                                                                .addGap(7, 7, 7)))
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(txtFechaHoraCertificacion, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                                                        .addComponent(txtFechaHoraEmision)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtDirecciónCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)))))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(jLabel19)
                                                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel20)
                                                                .addGap(60, 60, 60)
                                                                .addComponent(jLabel25)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(lblSubtotal))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(BttnAgregar)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                                                                .addGap(20, 20, 20)))
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addGap(53, 53, 53)
                                                                                .addComponent(jLabel27)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(lblIVA))
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jLabel29)
                                                                                .addGap(18, 18, 18)
                                                                                .addComponent(lblTotal)))))))
                                .addGap(41, 41, 41))
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(57, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jSeparator1)
                                                        .addComponent(jSeparator3))
                                                .addContainerGap())))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                                        .addContainerGap()))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel1)
                                .addGap(28, 28, 28)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel22))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel12)
                                        .addComponent(txtNitReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15)
                                        .addComponent(txtFechaHoraEmision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel16)
                                                .addComponent(txtFechaHoraCertificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel13)
                                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(txtDirecciónCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblSubtotal)
                                        .addComponent(jLabel25)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18)
                                        .addComponent(jLabel19)
                                        .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblIVA)
                                        .addComponent(jLabel27)
                                        .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(BttnAgregar)
                                        .addComponent(lblTotal)
                                        .addComponent(jLabel29))
                                .addGap(22, 22, 22)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(48, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addGap(180, 180, 180)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(657, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>

    private void txtNitReceptorActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtFechaHoraEmisionActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtDescuentoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void BttnAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String descripcion = txtDescripcion.getText();
            double precio = Double.parseDouble(txtPrecio.getText());
            double descuento = txtDescuento.getText().isEmpty() ? 0.0 : Double.parseDouble(txtDescuento.getText());

            Producto producto = new Producto();
            producto.setNombre(descripcion);
            producto.setPrecio(precio);

            DetalleFactura detalle = new DetalleFactura();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precio);

            facturaActual.agegarDetalle(detalle);

            double totalLinea = detalle.calcularSubtotal() - descuento;

            javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel)tblDetalleFactura.getModel();
            modelo.addRow(new Object[]{modelo.getRowCount() + 1, "B", cantidad, descripcion, precio, descuento, totalLinea});

            recalcularTotales(modelo);

            txtCantidad.setText("");
            txtDescripcion.setText("");
            txtPrecio.setText("");
            txtDescuento.setText("");

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, ingresa valores numéricos válidos en Cantidad, Precio y Descuento.");
        }
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

        java.awt.EventQueue.invokeLater(() -> new FrmFactura().setVisible(true));
    }

    // Variables declaration - do not modify
    private javax.swing.JButton BttnAgregar;
    private javax.swing.JButton BttnGuardar;
    private javax.swing.JButton BttnImprimir;
    private javax.swing.JButton BttnLimpiar;
    private javax.swing.JButton BttnEliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblIVA;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblDetalleFactura;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDescuento;
    private javax.swing.JTextField txtDirecciónCliente;
    private javax.swing.JTextField txtFechaHoraCertificacion;
    private javax.swing.JTextField txtFechaHoraEmision;
    private javax.swing.JTextField txtNitReceptor;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration
}
