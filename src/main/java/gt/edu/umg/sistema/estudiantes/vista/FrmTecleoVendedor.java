package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.VendedorController;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
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

public class FrmTecleoVendedor extends JInternalFrame {

    private final VendedorController controller;
    private final Runnable alGuardar;
    private Vendedor actual;

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtCorreo = new JTextField(24);
    private final JTextField txtTelefono = new JTextField(16);
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});

    public FrmTecleoVendedor(VendedorController controller, Vendedor vendedor, Runnable alGuardar) {
        super("Vendedor - Registro / Edición", true, true, true, true);
        this.controller = controller;
        this.alGuardar = alGuardar;
        this.actual = vendedor;
        setSize(480, 320);
        setLocation(100, 70);

        txtId.setEditable(false);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del Vendedor"));
        FormularioHelper.agregarCampo(form, 0, "Id:", txtId);
        FormularioHelper.agregarCampo(form, 1, "Nombre Completo:", txtNombre);
        FormularioHelper.agregarCampo(form, 2, "Correo Electrónico:", txtCorreo);
        FormularioHelper.agregarCampo(form, 3, "Teléfono:", txtTelefono);
        FormularioHelper.agregarCampo(form, 4, "Estado:", cmbEstado);

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
        txtId.setEditable(false);
        txtNombre.setText(FormularioHelper.textoSeguro(actual.getNombre()));
        txtCorreo.setText(FormularioHelper.textoSeguro(actual.getCorreo()));
        txtTelefono.setText(FormularioHelper.textoSeguro(actual.getTelefono()));
        cmbEstado.setSelectedItem(actual.getEstado() != null ? actual.getEstado() : "ACTIVO");
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtId.setEditable(true);
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        cmbEstado.setSelectedItem("ACTIVO");
        txtNombre.requestFocus();
    }

    private void grabar() {
        try {
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
                if (controller.buscarPorId(idIngresado) != null) {
                    JOptionPane.showMessageDialog(this, "El ID " + idIngresado + " ya existe. Ingrese un ID diferente o déjelo vacío para autogenerar.", "ID Duplicado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            Vendedor vendedor = actual == null ? new Vendedor() : actual;
            if (actual == null) {
                vendedor.setId(idIngresado);
            }
            vendedor.setNombre(FormularioHelper.texto(txtNombre));
            vendedor.setCorreo(FormularioHelper.texto(txtCorreo));
            vendedor.setTelefono(FormularioHelper.texto(txtTelefono));
            vendedor.setEstado((String) cmbEstado.getSelectedItem());

            controller.guardar(vendedor);
            actual = vendedor;
            txtId.setText(String.valueOf(vendedor.getId()));
            txtId.setEditable(false);
            JOptionPane.showMessageDialog(this, "Vendedor guardado exitosamente.");
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
