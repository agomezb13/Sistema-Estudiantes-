package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.PagoController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FrmTecleoPago extends JInternalFrame {

    private final PagoController pagoController;
    private final PedidoController pedidoController;
    private final Runnable alGuardar;
    private Pago actual;

    private final JTextField txtId = new JTextField(8);
    private final JComboBox<Pedido> cmbPedido;
    private final JTextField txtMonto = new JTextField(12);
    private final JComboBox<String> cmbMetodo = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA", "CHEQUE"});
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"PENDIENTE", "PAGADO", "REEMBOLSADO"});

    public FrmTecleoPago(PagoController pagoController, PedidoController pedidoController, Pago pago, Runnable alGuardar) {
        super("Pago - Registro / Procesamiento", true, true, true, true);
        this.pagoController = pagoController;
        this.pedidoController = pedidoController;
        this.actual = pago;
        this.alGuardar = alGuardar;

        setSize(520, 360);
        setLocation(90, 60);

        this.cmbPedido = FormularioHelper.comboConOpcionVacia(pedidoController.listar(), "-- Seleccione Pedido --");
        txtId.setEditable(false);

        cmbPedido.addActionListener(e -> {
            Pedido p = (Pedido) cmbPedido.getSelectedItem();
            if (p != null && (actual == null || txtMonto.getText().trim().isEmpty())) {
                txtMonto.setText(String.format("%.2f", p.getTotal()));
            }
        });

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del Pago"));
        FormularioHelper.agregarCampo(form, 0, "Id Pago:", txtId);
        FormularioHelper.agregarCampo(form, 1, "Pedido:", cmbPedido);
        FormularioHelper.agregarCampo(form, 2, "Monto (Q):", txtMonto);
        FormularioHelper.agregarCampo(form, 3, "Método de Pago:", cmbMetodo);
        FormularioHelper.agregarCampo(form, 4, "Estado:", cmbEstado);

        JButton btnGrabar = new JButton("Grabar");
        JButton btnProcesar = new JButton("Marcar Pagado");
        JButton btnReembolsar = new JButton("Reembolsar");
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnCancelar = new JButton("Cancelar");

        btnGrabar.addActionListener(e -> grabar());
        btnProcesar.addActionListener(e -> {
            cmbEstado.setSelectedItem("PAGADO");
            grabar();
        });
        btnReembolsar.addActionListener(e -> {
            cmbEstado.setSelectedItem("REEMBOLSADO");
            grabar();
        });
        btnNuevo.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnGrabar);
        botones.add(btnProcesar);
        botones.add(btnReembolsar);
        botones.add(btnNuevo);
        botones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
        cargar();
    }

    private void cargar() {
        if (actual == null) {
            limpiar();
            return;
        }

        txtId.setText(String.valueOf(actual.getId()));
        txtMonto.setText(String.format("%.2f", actual.getMonto()));
        cmbMetodo.setSelectedItem(actual.getMetodo() != null ? actual.getMetodo() : "EFECTIVO");
        cmbEstado.setSelectedItem(actual.getEstado() != null ? actual.getEstado() : "PENDIENTE");

        for (int i = 0; i < cmbPedido.getItemCount(); i++) {
            Pedido p = cmbPedido.getItemAt(i);
            if (p != null && p.getId() == actual.getPedidoId()) {
                cmbPedido.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtMonto.setText("");
        cmbMetodo.setSelectedIndex(0);
        cmbEstado.setSelectedItem("PENDIENTE");
        if (cmbPedido.getItemCount() > 0) {
            cmbPedido.setSelectedIndex(0);
        }
    }

    private void grabar() {
        Pedido ped = (Pedido) cmbPedido.getSelectedItem();
        if (ped == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un pedido para asociar el pago.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(txtMonto.getText().trim());
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a cero.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto ingresado no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Pago p = actual == null ? new Pago() : actual;
            p.setPedidoId(ped.getId());
            p.setMonto(monto);
            p.setMetodo((String) cmbMetodo.getSelectedItem());
            p.setEstado((String) cmbEstado.getSelectedItem());

            pagoController.guardar(p);
            JOptionPane.showMessageDialog(this, "Pago guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (alGuardar != null) {
                alGuardar.run();
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar el pago: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
