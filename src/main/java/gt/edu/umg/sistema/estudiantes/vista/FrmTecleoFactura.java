package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.controlador.FacturaController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.controlador.VendedorController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetalleFactura;
import gt.edu.umg.sistema.estudiantes.modelo.DetallePedido;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmTecleoFactura extends JInternalFrame {

    private final FacturaController facturaController;
    private final ClienteController clienteController;
    private final VendedorController vendedorController;
    private final PedidoController pedidoController;
    private final ProductoController productoController;
    private final Runnable alGuardar;
    private Factura actual;

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNumero = new JTextField(16);
    private final JComboBox<Cliente> cmbCliente;
    private final JComboBox<Vendedor> cmbVendedor;
    private final JComboBox<Pedido> cmbPedido;
    private final JTextField txtFechaEmision = new JTextField(14);
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"EMITIDA", "PAGADA", "ANULADA"});

    private final JComboBox<Producto> cmbProducto;
    private final JTextField txtCantidad = new JTextField("1", 5);
    private final JTextField txtPrecioUnitario = new JTextField(7);
    private final JLabel lblStockDisponible = new JLabel("Stock disp.: -");

    private final DefaultTableModel modeloDetalle = FormularioHelper.modeloNoEditable(
            new String[]{"ID Prod", "Producto", "Cantidad", "Precio Unitario", "Subtotal"}
    );
    private final JTable tablaDetalle = new JTable(modeloDetalle);

    private final JLabel lblSubtotal = new JLabel("Subtotal: Q. 0.00");
    private final JLabel lblIva = new JLabel("IVA (12%): Q. 0.00");
    private final JLabel lblTotal = new JLabel("TOTAL: Q. 0.00");

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final List<DetalleFactura> detallesLocales = new ArrayList<>();

    public FrmTecleoFactura(FacturaController facturaController, ClienteController clienteController,
                            VendedorController vendedorController, PedidoController pedidoController,
                            ProductoController productoController, Factura factura, Runnable alGuardar) {
        super("Factura - Emisión / Consulta", true, true, true, true);
        this.facturaController = facturaController;
        this.clienteController = clienteController;
        this.vendedorController = vendedorController;
        this.pedidoController = pedidoController;
        this.productoController = productoController;
        this.actual = factura;
        this.alGuardar = alGuardar;

        setSize(780, 620);
        setLocation(50, 20);

        this.cmbCliente = FormularioHelper.comboConOpcionVacia(clienteController.listar(), "-- Seleccione Cliente --");
        this.cmbVendedor = FormularioHelper.comboConOpcionVacia(vendedorController.listar(), "-- Seleccione Vendedor --");
        this.cmbPedido = FormularioHelper.comboConOpcionVacia(pedidoController.listar(), "-- Sin Pedido Asociado --");
        this.cmbProducto = FormularioHelper.comboConOpcionVacia(productoController.listar(), "-- Seleccione Producto --");

        txtId.setEditable(false);
        txtFechaEmision.setEditable(false);
        txtPrecioUnitario.setEditable(false);

        lblSubtotal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblIva.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 14));

        inicializarEventos();
        construirInterfaz();
        cargar();
    }

    private void inicializarEventos() {
        cmbProducto.addActionListener(e -> {
            Producto prod = (Producto) cmbProducto.getSelectedItem();
            if (prod != null) {
                txtPrecioUnitario.setText(String.format("%.2f", prod.getPrecio()));
                lblStockDisponible.setText("Stock disp.: " + prod.getExistencias());
            } else {
                txtPrecioUnitario.setText("");
                lblStockDisponible.setText("Stock disp.: -");
            }
        });

        cmbPedido.addActionListener(e -> {
            Pedido p = (Pedido) cmbPedido.getSelectedItem();
            if (p != null && actual == null) {
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    Cliente c = cmbCliente.getItemAt(i);
                    if (c != null && c.getId() == p.getClienteId()) {
                        cmbCliente.setSelectedIndex(i);
                        break;
                    }
                }

                if (p.getDetalles() != null && !p.getDetalles().isEmpty()) {
                    detallesLocales.clear();
                    modeloDetalle.setRowCount(0);
                    for (DetallePedido dp : p.getDetalles()) {
                        DetalleFactura df = new DetalleFactura(0, 0, dp.getProductoId(), dp.getCantidad(), dp.getPrecio(), dp.getSubtotal());
                        detallesLocales.add(df);
                        Producto pr = productoController.buscarPorId(dp.getProductoId());
                        String nom = pr != null ? pr.getNombre() : "Producto #" + dp.getProductoId();
                        modeloDetalle.addRow(new Object[]{
                            dp.getProductoId(),
                            nom,
                            dp.getCantidad(),
                            String.format("%.2f", dp.getPrecio()),
                            String.format("%.2f", dp.getSubtotal())
                        });
                    }
                    recalcularTotales();
                }
            }
        });
    }

    private void construirInterfaz() {
        JPanel panelCabecera = new JPanel(new GridBagLayout());
        panelCabecera.setBorder(BorderFactory.createTitledBorder("Encabezado de Factura"));
        FormularioHelper.agregarCampo(panelCabecera, 0, "No. ID Interno:", txtId);
        FormularioHelper.agregarCampo(panelCabecera, 1, "No. Factura:", txtNumero);
        FormularioHelper.agregarCampo(panelCabecera, 2, "Cliente:", cmbCliente);
        FormularioHelper.agregarCampo(panelCabecera, 3, "Vendedor:", cmbVendedor);
        FormularioHelper.agregarCampo(panelCabecera, 4, "Pedido Asociado:", cmbPedido);
        FormularioHelper.agregarCampo(panelCabecera, 5, "Fecha Emisión:", txtFechaEmision);
        FormularioHelper.agregarCampo(panelCabecera, 6, "Estado:", cmbEstado);

        JPanel panelAgregarItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelAgregarItem.setBorder(BorderFactory.createTitledBorder("Agregar Ítems a la Factura"));
        panelAgregarItem.add(new JLabel("Producto:"));
        panelAgregarItem.add(cmbProducto);
        panelAgregarItem.add(lblStockDisponible);
        panelAgregarItem.add(new JLabel("Cant:"));
        panelAgregarItem.add(txtCantidad);
        panelAgregarItem.add(new JLabel("Precio:"));
        panelAgregarItem.add(txtPrecioUnitario);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnQuitar = new JButton("Quitar Seleccionado");
        btnAgregar.addActionListener(e -> agregarDetalle());
        btnQuitar.addActionListener(e -> quitarDetalle());

        panelAgregarItem.add(btnAgregar);
        panelAgregarItem.add(btnQuitar);

        JScrollPane scrollTabla = new JScrollPane(tablaDetalle);
        scrollTabla.setPreferredSize(new Dimension(720, 160));

        JPanel panelTotales = new JPanel(new GridLayout(3, 1, 4, 4));
        panelTotales.setBorder(BorderFactory.createTitledBorder("Resumen de Cobro"));
        panelTotales.add(lblSubtotal);
        panelTotales.add(lblIva);
        panelTotales.add(lblTotal);

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(panelAgregarItem, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);
        panelCentro.add(panelTotales, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGrabar = new JButton("Grabar Factura");
        JButton btnAnular = new JButton("Anular Factura");
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnCancelar = new JButton("Cancelar");

        btnGrabar.addActionListener(e -> grabar());
        btnAnular.addActionListener(e -> anularFactura());
        btnNuevo.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGrabar);
        panelBotones.add(btnAnular);
        panelBotones.add(btnNuevo);
        panelBotones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(panelCabecera, BorderLayout.NORTH);
        getContentPane().add(panelCentro, BorderLayout.CENTER);
        getContentPane().add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarDetalle() {
        Producto prod = (Producto) cmbProducto.getSelectedItem();
        if (prod == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad ingresada no es válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int cantidadYaAgregada = 0;
        for (DetalleFactura d : detallesLocales) {
            if (d.getProductoId() == prod.getId()) {
                cantidadYaAgregada += d.getCantidad();
            }
        }
        if (cantidadYaAgregada + cantidad > prod.getExistencias()) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuficiente para '" + prod.getNombre() + "'.\n"
                    + "Existencias en bodega: " + prod.getExistencias() + "\n"
                    + "Ya agregadas al detalle: " + cantidadYaAgregada + "\n"
                    + "Intenta agregar: " + cantidad,
                    "Stock Insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio = prod.getPrecio();
        double subtotal = precio * cantidad;

        DetalleFactura det = new DetalleFactura(0, 0, prod.getId(), cantidad, precio, subtotal);
        detallesLocales.add(det);

        modeloDetalle.addRow(new Object[]{
            prod.getId(),
            prod.getNombre(),
            cantidad,
            String.format("%.2f", precio),
            String.format("%.2f", subtotal)
        });

        recalcularTotales();
        txtCantidad.setText("1");
        cmbProducto.setSelectedIndex(0);
    }

    private void quitarDetalle() {
        int fila = tablaDetalle.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle para quitar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        detallesLocales.remove(fila);
        modeloDetalle.removeRow(fila);
        recalcularTotales();
    }

    private void recalcularTotales() {
        double subtotal = 0.0;
        for (DetalleFactura det : detallesLocales) {
            subtotal += det.calcularSubtotal();
        }
        double iva = subtotal * 0.12;
        double total = subtotal + iva;

        lblSubtotal.setText(String.format("Subtotal: Q. %.2f", subtotal));
        lblIva.setText(String.format("IVA (12%%): Q. %.2f", iva));
        lblTotal.setText(String.format("TOTAL: Q. %.2f", total));
    }

    private void cargar() {
        if (actual == null) {
            limpiar();
            return;
        }

        txtId.setText(String.valueOf(actual.getId()));
        txtId.setEditable(false);
        txtNumero.setText(FormularioHelper.textoSeguro(actual.getNumero()));
        txtFechaEmision.setText(actual.getFechaEmision() != null ? sdf.format(actual.getFechaEmision()) : "");
        cmbEstado.setSelectedItem(actual.getEstado() != null ? actual.getEstado() : "EMITIDA");

        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            Cliente c = cmbCliente.getItemAt(i);
            if (c != null && c.getId() == actual.getClienteId()) {
                cmbCliente.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cmbVendedor.getItemCount(); i++) {
            Vendedor v = cmbVendedor.getItemAt(i);
            if (v != null && v.getId() == actual.getVendedorId()) {
                cmbVendedor.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cmbPedido.getItemCount(); i++) {
            Pedido p = cmbPedido.getItemAt(i);
            if (p != null && p.getId() == actual.getPedidoId()) {
                cmbPedido.setSelectedIndex(i);
                break;
            }
        }

        detallesLocales.clear();
        modeloDetalle.setRowCount(0);
        if (actual.getDetalles() != null) {
            for (DetalleFactura d : actual.getDetalles()) {
                detallesLocales.add(d);
                Producto prod = productoController.buscarPorId(d.getProductoId());
                String nomProd = prod != null ? prod.getNombre() : "Producto #" + d.getProductoId();
                modeloDetalle.addRow(new Object[]{
                    d.getProductoId(),
                    nomProd,
                    d.getCantidad(),
                    String.format("%.2f", d.getPrecioUnitario()),
                    String.format("%.2f", d.getSubtotal())
                });
            }
        }
        recalcularTotales();
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtId.setEditable(true);
        txtNumero.setText("FAC-" + System.currentTimeMillis() % 100000);
        txtFechaEmision.setText(sdf.format(new Date()));
        cmbEstado.setSelectedItem("EMITIDA");
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        if (cmbVendedor.getItemCount() > 0) cmbVendedor.setSelectedIndex(0);
        if (cmbPedido.getItemCount() > 0) cmbPedido.setSelectedIndex(0);
        detallesLocales.clear();
        modeloDetalle.setRowCount(0);
        recalcularTotales();
        txtCantidad.setText("1");
    }

    private void anularFactura() {
        if (actual == null || actual.getId() <= 0) {
            JOptionPane.showMessageDialog(this, "Debe cargar una factura existente para anularla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this, "¿Está seguro de anular la factura " + actual.getNumero() + "?", "Confirmar Anulación", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            facturaController.anular(actual.getId());
            actual.anular();
            cmbEstado.setSelectedItem("ANULADA");
            JOptionPane.showMessageDialog(this, "Factura anulada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (alGuardar != null) alGuardar.run();
        }
    }

    private void grabar() {
        String num = txtNumero.getText().trim();
        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de factura es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = (Cliente) cmbCliente.getSelectedItem();
        if (cli == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (detallesLocales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la factura.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idIngresado = 0;
        String txtIdVal = txtId.getText().trim();
        if (!txtIdVal.isEmpty()) {
            try {
                idIngresado = Integer.parseInt(txtIdVal);
                if (idIngresado <= 0) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número entero positivo.", "ID Inválido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.", "ID Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (actual == null && idIngresado > 0) {
            if (facturaController.buscarPorId(idIngresado) != null) {
                JOptionPane.showMessageDialog(this, "El ID " + idIngresado + " ya existe. Ingrese un ID diferente o déjelo vacío para autogenerar.", "ID Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            Factura f = actual == null ? new Factura() : actual;
            if (actual == null) {
                f.setId(idIngresado);
            }
            f.setNumero(num);
            f.setClienteId(cli.getId());

            Vendedor ven = (Vendedor) cmbVendedor.getSelectedItem();
            f.setVendedorId(ven != null ? ven.getId() : 0);

            Pedido ped = (Pedido) cmbPedido.getSelectedItem();
            f.setPedidoId(ped != null ? ped.getId() : 0);

            f.setEstado((String) cmbEstado.getSelectedItem());
            f.getDetalles().clear();
            f.getDetalles().addAll(detallesLocales);
            f.calcularTotal();

            facturaController.guardar(f);
            actual = f;
            txtId.setText(String.valueOf(f.getId()));
            txtId.setEditable(false);
            JOptionPane.showMessageDialog(this, "Factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (alGuardar != null) {
                alGuardar.run();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
