package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DlgRegistroComprador extends JDialog {

    private final ClienteController controller;
    private final Consumer<Cliente> onRegistrado;

    private final JTextField txtNombre = new JTextField(22);
    private final JTextField txtCorreo = new JTextField(22);
    private final JTextField txtTelefono = new JTextField(15);
    private final JTextField txtDireccion = new JTextField(22);
    private final JTextField txtDpi = new JTextField(15);
    private final JTextField txtNit = new JTextField(15);

    public DlgRegistroComprador(Frame parent, ClienteController controller, Consumer<Cliente> onRegistrado) {
        super(parent, "Registro de Nuevo Comprador", true);
        this.controller = controller;
        this.onRegistrado = onRegistrado;

        setSize(480, 360);
        setLocationRelativeTo(parent);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos del Nuevo Comprador"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        FormularioHelper.agregarCampo(form, 0, "Nombre Completo *:", txtNombre);
        FormularioHelper.agregarCampo(form, 1, "Correo Electrónico *:", txtCorreo);
        FormularioHelper.agregarCampo(form, 2, "Teléfono:", txtTelefono);
        FormularioHelper.agregarCampo(form, 3, "Dirección de Entrega:", txtDireccion);
        FormularioHelper.agregarCampo(form, 4, "No. DPI:", txtDpi);
        FormularioHelper.agregarCampo(form, 5, "NIT:", txtNit);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnRegistrar = new JButton("Registrar y Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");

        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> dispose());

        botones.add(btnRegistrar);
        botones.add(btnCancelar);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    private void registrar() {
        String nom = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del comprador es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }

        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El correo electrónico es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return;
        }

        try {
            Cliente nuevo = new Cliente();
            nuevo.setNombre(nom);
            nuevo.setCorreo(correo);
            nuevo.setTelefono(txtTelefono.getText().trim());
            nuevo.setDireccion(txtDireccion.getText().trim());
            nuevo.setNumeroDPI(txtDpi.getText().trim());
            nuevo.setNIT(txtNit.getText().trim());
            nuevo.setEstado("ACTIVO");

            controller.guardar(nuevo);
            JOptionPane.showMessageDialog(this, "Comprador registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (onRegistrado != null) {
                onRegistrado.accept(nuevo);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar comprador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
