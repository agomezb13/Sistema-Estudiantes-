package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
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

public class FrmTecleoCliente extends JInternalFrame {

    private final ClienteController controller;
    private final Runnable alGuardar;
    private Cliente actual;

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtCorreo = new JTextField(24);
    private final JTextField txtTelefono = new JTextField(16);
    private final JTextField txtDireccion = new JTextField(24);
    private final JTextField txtDpi = new JTextField(16);
    private final JTextField txtNit = new JTextField(16);
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});

    public FrmTecleoCliente(ClienteController controller, Cliente cliente, Runnable alGuardar) {
        super("Cliente - Registro / Edición", true, true, true, true);
        this.controller = controller;
        this.alGuardar = alGuardar;
        this.actual = cliente;
        setSize(520, 420);
        setLocation(80, 50);

        txtId.setEditable(false);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        FormularioHelper.agregarCampo(form, 0, "Id:", txtId);
        FormularioHelper.agregarCampo(form, 1, "Nombre Completo:", txtNombre);
        FormularioHelper.agregarCampo(form, 2, "Correo Electrónico:", txtCorreo);
        FormularioHelper.agregarCampo(form, 3, "Teléfono:", txtTelefono);
        FormularioHelper.agregarCampo(form, 4, "Dirección:", txtDireccion);
        FormularioHelper.agregarCampo(form, 5, "No. DPI:", txtDpi);
        FormularioHelper.agregarCampo(form, 6, "NIT:", txtNit);
        FormularioHelper.agregarCampo(form, 7, "Estado:", cmbEstado);

        JButton btnGrabar = new JButton("Grabar");
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnCancelar = new JButton("Cancelar");

        btnGrabar.addActionListener(e -> grabar());
        btnNuevo.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnGrabar);
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
        txtNombre.setText(FormularioHelper.textoSeguro(actual.getNombre()));
        txtCorreo.setText(FormularioHelper.textoSeguro(actual.getCorreo()));
        txtTelefono.setText(FormularioHelper.textoSeguro(actual.getTelefono()));
        txtDireccion.setText(FormularioHelper.textoSeguro(actual.getDireccion()));
        txtDpi.setText(FormularioHelper.textoSeguro(actual.getNumeroDPI()));
        txtNit.setText(FormularioHelper.textoSeguro(actual.getNIT()));
        cmbEstado.setSelectedItem(actual.getEstado() != null ? actual.getEstado() : "ACTIVO");
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        txtDpi.setText("");
        txtNit.setText("");
        cmbEstado.setSelectedItem("ACTIVO");
        txtNombre.requestFocus();
    }

    private void grabar() {
        try {
            Cliente cliente = actual == null ? new Cliente() : actual;
            cliente.setNombre(FormularioHelper.texto(txtNombre));
            cliente.setCorreo(FormularioHelper.texto(txtCorreo));
            cliente.setTelefono(FormularioHelper.texto(txtTelefono));
            cliente.setDireccion(FormularioHelper.texto(txtDireccion));
            cliente.setNumeroDPI(FormularioHelper.texto(txtDpi));
            cliente.setNIT(FormularioHelper.texto(txtNit));
            cliente.setEstado((String) cmbEstado.getSelectedItem());

            controller.guardar(cliente);
            actual = cliente;
            txtId.setText(String.valueOf(cliente.getId()));
            JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
