package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FrmTecleoCategoria extends JInternalFrame {

    private final CategoriaController controller;
    private final Runnable alGuardar;
    private Categoria actual;

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtDescripcion = new JTextField(24);

    public FrmTecleoCategoria(CategoriaController controller, Categoria categoria, Runnable alGuardar) {
        super("Categoría - Registro / Edición", true, true, true, true);
        this.controller = controller;
        this.alGuardar = alGuardar;
        this.actual = categoria;
        setSize(480, 260);
        setLocation(100, 80);

        txtId.setEditable(false);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos de la Categoría"));
        FormularioHelper.agregarCampo(form, 0, "Id:", txtId);
        FormularioHelper.agregarCampo(form, 1, "Nombre:", txtNombre);
        FormularioHelper.agregarCampo(form, 2, "Descripción:", txtDescripcion);

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
        txtDescripcion.setText(FormularioHelper.textoSeguro(actual.getDescripcion()));
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtId.setEditable(true);
        txtNombre.setText("");
        txtDescripcion.setText("");
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

            Categoria categoria = actual == null ? new Categoria() : actual;
            if (actual == null) {
                categoria.setId(idIngresado);
            }
            categoria.setNombre(FormularioHelper.texto(txtNombre));
            categoria.setDescripcion(FormularioHelper.texto(txtDescripcion));

            controller.guardar(categoria);
            actual = categoria;
            txtId.setText(String.valueOf(categoria.getId()));
            txtId.setEditable(false);
            JOptionPane.showMessageDialog(this, "Categoría guardada exitosamente.");
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
