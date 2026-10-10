package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.controlador.DireccionEnvioController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.DetallePedido;
import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
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

public class FrmTecleoPedido extends JInternalFrame {

    private final PedidoController pedidoController;
    private final ClienteController clienteController;
    private final ProductoController productoController;
    private final DireccionEnvioController direccionController;
    private final Runnable alGuardar;
    private Pedido actual;

    private final JTextField txtId = new JTextField(8);
    private final JComboBox<Cliente> cmbCliente;
    private final JComboBox<DireccionEnvio> cmbDireccion = new JComboBox<>();
    private final JTextField txtFecha = new JTextField(14);
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "CONFIRMADO", "CANCELADO"});

    private final JComboBox<Producto> cmbProducto;
    private final JTextField txtCantidad = new JTextField("1", 5);
    private final JTextField txtPrecioUnitario = new JTextField(7);
    private final JLabel lblStockDisponible = new JLabel("Stock disp.: -");

    private final DefaultTableModel modeloDetalle = FormularioHelper.modeloNoEditable(
            new String[]{"ID Prod", "Producto", "Cantidad", "Precio Unitario", "Subtotal"}
    );
    private final JTable tablaDetalle = new JTable(modeloDetalle);
    private final JLabel lblTotal = new JLabel("Total: Q. 0.00");

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final List<DetallePedido> detallesLocales = new ArrayList<>();

    private final Cliente clienteFijo;
    private final boolean esModoInvitado;
    private final JTextField txtNombreInvitado = new JTextField(20);
    private final JTextField txtDireccionInvitado = new JTextField(25);
    private final JTextField txtTelefonoInvitado = new JTextField(12);

    public FrmTecleoPedido(PedidoController pedidoController, ClienteController clienteController,
                            ProductoController productoController, DireccionEnvioController direccionController,
                            Pedido pedido, Runnable alGuardar) {
        this(pedidoController, clienteController, productoController, direccionController, pedido, alGuardar, null, false);
    }

    public FrmTecleoPedido(PedidoController pedidoController, ClienteController clienteController,
                            ProductoController productoController, DireccionEnvioController direccionController,
                            Pedido pedido, Runnable alGuardar, Cliente clienteFijo) {
        this(pedidoController, clienteController, productoController, direccionController, pedido, alGuardar, clienteFijo, false);
    }

    public FrmTecleoPedido(PedidoController pedidoController, ClienteController clienteController,
                            ProductoController productoController, DireccionEnvioController direccionController,
                            Pedido pedido, Runnable alGuardar, Cliente clienteFijo, boolean esModoInvitado) {
        super(esModoInvitado ? "Nueva Orden de Compra" : (clienteFijo != null ? "Orden de Compra - Registro" : "Pedido - Registro / Edición"), true, true, true, true);
        this.pedidoController = pedidoController;
        this.clienteController = clienteController;
        this.productoController = productoController;
        this.direccionController = direccionController;
        this.actual = pedido;
        this.alGuardar = alGuardar;
        this.clienteFijo = clienteFijo;
        this.esModoInvitado = esModoInvitado;

        setSize(740, 580);
        setLocation(60, 30);

        this.cmbCliente = FormularioHelper.comboConOpcionVacia(clienteController.listar(), "-- Seleccione Cliente --");
        this.cmbProducto = FormularioHelper.comboConOpcionVacia(productoController.listar(), "-- Seleccione Producto --");

        txtId.setEditable(false);
        txtFecha.setEditable(false);
        txtPrecioUnitario.setEditable(false);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 14));

        inicializarEventos();
        construirInterfaz();
        cargar();
    }

    private void inicializarEventos() {
        cmbCliente.addActionListener(e -> actualizarDireccionesCliente());

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
    }

    private void actualizarDireccionesCliente() {
        cmbDireccion.removeAllItems();
        Cliente cli = (Cliente) cmbCliente.getSelectedItem();
        if (cli != null) {
            List<DireccionEnvio> dirs = direccionController.listarPorCliente(cli.getId());
            for (DireccionEnvio d : dirs) {
                cmbDireccion.addItem(d);
            }
        }
    }

    private void construirInterfaz() {
        JPanel panelCabecera = new JPanel(new GridBagLayout());
        if (esModoInvitado) {
            panelCabecera.setBorder(BorderFactory.createTitledBorder("Datos del Comprador"));
            txtId.setText("");
            txtId.setEditable(false);
            FormularioHelper.agregarCampo(panelCabecera, 0, "Tu Nombre:", txtNombreInvitado);
            FormularioHelper.agregarCampo(panelCabecera, 1, "Dirección de Entrega:", txtDireccionInvitado);
            FormularioHelper.agregarCampo(panelCabecera, 2, "Teléfono / Contacto:", txtTelefonoInvitado);
            FormularioHelper.agregarCampo(panelCabecera, 3, "Fecha:", txtFecha);
        } else {
            panelCabecera.setBorder(BorderFactory.createTitledBorder("Datos Generales del Pedido"));
            FormularioHelper.agregarCampo(panelCabecera, 0, "No. Pedido:", txtId);
            FormularioHelper.agregarCampo(panelCabecera, 1, "Cliente:", cmbCliente);
            FormularioHelper.agregarCampo(panelCabecera, 2, "Dirección Envío:", cmbDireccion);
            FormularioHelper.agregarCampo(panelCabecera, 3, "Fecha:", txtFecha);
            FormularioHelper.agregarCampo(panelCabecera, 4, "Estado:", cmbEstado);
        }

        JPanel panelAgregarItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelAgregarItem.setBorder(BorderFactory.createTitledBorder("Agregar Ítems"));
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
        scrollTabla.setPreferredSize(new Dimension(680, 160));

        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.add(panelAgregarItem, BorderLayout.NORTH);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelTotal.add(lblTotal);
        panelCentro.add(panelTotal, BorderLayout.SOUTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGrabar = new JButton(esModoInvitado ? "Enviar Orden de Compra" : "Grabar Pedido");
        JButton btnNuevo = new JButton(esModoInvitado ? "Limpiar Campos" : "Nuevo");
        JButton btnCancelar = new JButton(esModoInvitado ? "Cerrar" : "Cancelar");

        btnGrabar.addActionListener(e -> grabar());
        btnNuevo.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGrabar);
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
        for (DetallePedido d : detallesLocales) {
            if (d.getProductoId() == prod.getId()) {
                cantidadYaAgregada += d.getCantidad();
            }
        }
        if (cantidadYaAgregada + cantidad > prod.getExistencias()) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuficiente para '" + prod.getNombre() + "'.\n"
                    + "Existencias en bodega: " + prod.getExistencias() + "\n"
                    + "Ya agregadas al pedido: " + cantidadYaAgregada + "\n"
                    + "Intenta agregar: " + cantidad,
                    "Stock Insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio = prod.getPrecio();
        double subtotal = precio * cantidad;

        DetallePedido det = new DetallePedido(0, 0, prod.getId(), cantidad, precio, subtotal);
        detallesLocales.add(det);

        modeloDetalle.addRow(new Object[]{
            prod.getId(),
            prod.getNombre(),
            cantidad,
            String.format("%.2f", precio),
            String.format("%.2f", subtotal)
        });

        recalcularTotal();
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
        recalcularTotal();
    }

    private void recalcularTotal() {
        double total = 0.0;
        for (DetallePedido det : detallesLocales) {
            total += det.getSubtotal();
        }
        lblTotal.setText(String.format("Total: Q. %.2f", total));
    }

    private void cargar() {
        if (actual == null) {
            limpiar();
            return;
        }

        txtId.setText(String.valueOf(actual.getId()));
        txtId.setEditable(false);
        txtFecha.setText(actual.getFecha() != null ? sdf.format(actual.getFecha()) : "");
        cmbEstado.setSelectedItem(actual.getEstado() != null ? actual.getEstado() : "PENDIENTE");

        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            Cliente c = cmbCliente.getItemAt(i);
            if (c != null && c.getId() == actual.getClienteId()) {
                cmbCliente.setSelectedIndex(i);
                break;
            }
        }

        actualizarDireccionesCliente();
        for (int i = 0; i < cmbDireccion.getItemCount(); i++) {
            DireccionEnvio d = cmbDireccion.getItemAt(i);
            if (d != null && d.getId() == actual.getDireccionEnvioId()) {
                cmbDireccion.setSelectedIndex(i);
                break;
            }
        }

        detallesLocales.clear();
        modeloDetalle.setRowCount(0);
        if (actual.getDetalles() != null) {
            for (DetallePedido d : actual.getDetalles()) {
                detallesLocales.add(d);
                Producto prod = productoController.buscarPorId(d.getProductoId());
                String nomProd = prod != null ? prod.getNombre() : "Producto #" + d.getProductoId();
                modeloDetalle.addRow(new Object[]{
                    d.getProductoId(),
                    nomProd,
                    d.getCantidad(),
                    String.format("%.2f", d.getPrecio()),
                    String.format("%.2f", d.getSubtotal())
                });
            }
        }
        recalcularTotal();
    }

    private void limpiar() {
        actual = null;
        if (esModoInvitado) {
            txtId.setText("");
            txtId.setEditable(false);
            txtNombreInvitado.setText("");
            txtDireccionInvitado.setText("");
            txtTelefonoInvitado.setText("");
            cmbEstado.setSelectedItem("PENDIENTE");
        } else {
            txtId.setText("");
            txtId.setEditable(true);
            cmbEstado.setSelectedItem("PENDIENTE");
            if (clienteFijo != null) {
                for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                    Cliente c = cmbCliente.getItemAt(i);
                    if (c != null && c.getId() == clienteFijo.getId()) {
                        cmbCliente.setSelectedIndex(i);
                        break;
                    }
                }
                cmbCliente.setEnabled(false);
                actualizarDireccionesCliente();
            } else if (cmbCliente.getItemCount() > 0) {
                cmbCliente.setSelectedIndex(0);
            }
            if (clienteFijo == null) {
                cmbDireccion.removeAllItems();
            }
        }
        txtFecha.setText(sdf.format(new Date()));
        detallesLocales.clear();
        modeloDetalle.setRowCount(0);
        recalcularTotal();
        txtCantidad.setText("1");
    }

    private void grabar() {
        if (esModoInvitado) {
            String nombre = txtNombreInvitado.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar su nombre para generar la orden.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtNombreInvitado.requestFocus();
                return;
            }

            String direccion = txtDireccionInvitado.getText().trim();
            if (direccion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar su dirección de entrega.", "Validación", JOptionPane.WARNING_MESSAGE);
                txtDireccionInvitado.requestFocus();
                return;
            }

            if (detallesLocales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto a la orden de compra.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String telefono = txtTelefonoInvitado.getText().trim();

            try {
                Cliente cli = null;
                for (Cliente c : clienteController.listar()) {
                    if (c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombre)) {
                        cli = c;
                        break;
                    }
                }
                if (cli == null) {
                    cli = new Cliente();
                    cli.setNombre(nombre);
                    cli.setCorreo(nombre.toLowerCase().replaceAll("[^a-zA-Z0-9]", "") + "@invitado.com");
                    cli.setDireccion(direccion);
                    cli.setTelefono(telefono);
                    cli.setEstado("ACTIVO");
                    clienteController.guardar(cli);
                }

                DireccionEnvio dir = new DireccionEnvio();
                dir.setClienteId(cli.getId());
                dir.setCalle(direccion);
                dir.setCiudad("Guatemala");
                dir.setCodigoPostal("01001");
                dir.setPais("Guatemala");
                direccionController.guardar(dir);

                Pedido p = new Pedido();
                p.setId(0);
                p.setClienteId(cli.getId());
                p.setDireccionEnvioId(dir.getId());
                p.setEstado("PENDIENTE");
                p.getDetalles().clear();
                p.getDetalles().addAll(detallesLocales);
                p.calcularTotal();

                pedidoController.guardar(p);
                actual = p;
                txtId.setText(String.valueOf(p.getId()));

                JOptionPane.showMessageDialog(this,
                        "¡Orden de compra generada exitosamente!\n\n"
                        + "Número de Orden: #" + p.getId() + "\n"
                        + "Cliente: " + cli.getNombre() + "\n"
                        + "Total: Q. " + String.format("%.2f", p.getTotal()) + "\n"
                        + "Estado: PENDIENTE",
                        "Orden Creada",
                        JOptionPane.INFORMATION_MESSAGE);

                if (alGuardar != null) {
                    alGuardar.run();
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la orden de compra: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        Cliente cli = (Cliente) cmbCliente.getSelectedItem();
        if (cli == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (detallesLocales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto al pedido.", "Validación", JOptionPane.WARNING_MESSAGE);
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
            if (pedidoController.buscarPorId(idIngresado) != null) {
                JOptionPane.showMessageDialog(this, "El ID " + idIngresado + " ya existe. Ingrese un ID diferente o déjelo vacío para autogenerar.", "ID Duplicado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        try {
            Pedido p = actual == null ? new Pedido() : actual;
            if (actual == null) {
                p.setId(idIngresado);
            }
            p.setClienteId(cli.getId());

            DireccionEnvio dir = (DireccionEnvio) cmbDireccion.getSelectedItem();
            p.setDireccionEnvioId(dir != null ? dir.getId() : 0);

            p.setEstado((String) cmbEstado.getSelectedItem());
            p.getDetalles().clear();
            p.getDetalles().addAll(detallesLocales);
            p.calcularTotal();

            pedidoController.guardar(p);
            actual = p;
            txtId.setText(String.valueOf(p.getId()));
            txtId.setEditable(false);
            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (alGuardar != null) {
                alGuardar.run();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el pedido: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
