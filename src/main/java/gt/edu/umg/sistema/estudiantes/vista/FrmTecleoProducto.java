package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
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

public class FrmTecleoProducto extends JInternalFrame {

    private final ProductoController productoController;
    private final CategoriaController categoriaController;
    private final Runnable alGuardar;
    private Producto actual;

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNombre = new JTextField(24);
    private final JComboBox<Categoria> cmbCategoria;
    private final JTextField txtPrecio = new JTextField(12);
    private final JTextField txtExistencias = new JTextField(12);
    private final JTextField txtDescripcion = new JTextField(24);

    public FrmTecleoProducto(ProductoController productoController, CategoriaController categoriaController, Producto producto, Runnable alGuardar) {
        super("Producto - Registro / Edición", true, true, true, true);
        this.productoController = productoController;
        this.categoriaController = categoriaController;
        this.alGuardar = alGuardar;
        this.actual = producto;
        this.cmbCategoria = FormularioHelper.comboConOpcionVacia(categoriaController.listar(), "-- Seleccione Categoría --");

        setSize(520, 360);
        setLocation(90, 60);

        txtId.setEditable(false);
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        FormularioHelper.agregarCampo(form, 0, "Id:", txtId);
        FormularioHelper.agregarCampo(form, 1, "Nombre:", txtNombre);
        FormularioHelper.agregarCampo(form, 2, "Categoría:", cmbCategoria);
        FormularioHelper.agregarCampo(form, 3, "Precio Unitario (Q):", txtPrecio);
        FormularioHelper.agregarCampo(form, 4, "Existencias:", txtExistencias);
        FormularioHelper.agregarCampo(form, 5, "Descripción:", txtDescripcion);

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
        txtPrecio.setText(String.format("%.2f", actual.getPrecio()).replace(",", "."));
        txtExistencias.setText(String.valueOf(actual.getExistencias()));
        txtDescripcion.setText(FormularioHelper.textoSeguro(actual.getDescripcion()));

        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            Categoria c = cmbCategoria.getItemAt(i);
            if (c != null && c.getId() == actual.getCategoriaId()) {
                cmbCategoria.setSelectedIndex(i);
                break;
            }
        }
    }

    private void limpiar() {
        actual = null;
        txtId.setText("");
        txtNombre.setText("");
        cmbCategoria.setSelectedItem(null);
        txtPrecio.setText("");
        txtExistencias.setText("");
        txtDescripcion.setText("");
        txtNombre.requestFocus();
    }

    private void grabar() {
        try {
            Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
            if (cat == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una categoría.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(FormularioHelper.texto(txtPrecio).replace(",", "."));
            int stock = Integer.parseInt(FormularioHelper.texto(txtExistencias));

            Producto producto = actual == null ? new Producto() : actual;
            producto.setNombre(FormularioHelper.texto(txtNombre));
            producto.setCategoriaId(cat.getId());
            producto.setPrecio(precio);
            producto.setExistencias(stock);
            producto.setDescripcion(FormularioHelper.texto(txtDescripcion));

            productoController.guardar(producto);
            actual = producto;
            txtId.setText(String.valueOf(producto.getId()));
            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente.");
            if (alGuardar != null) {
                alGuardar.run();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Existencias deben ser valores numéricos válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
