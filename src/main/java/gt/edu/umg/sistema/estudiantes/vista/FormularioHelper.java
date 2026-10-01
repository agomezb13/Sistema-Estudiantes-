package gt.edu.umg.sistema.estudiantes.vista;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public final class FormularioHelper {

    private FormularioHelper() {
    }

    public static void agregarCampo(JPanel panel, int fila, String etiqueta, JComponent campo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    public static DefaultTableModel modeloNoEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static String texto(JTextField campo) {
        return campo.getText() == null ? "" : campo.getText().trim();
    }

    public static String textoSeguro(String valor) {
        return valor == null ? "" : valor;
    }

    public static <T> JComboBox<T> comboConOpcionVacia(List<T> items, String textoVacio) {
        DefaultComboBoxModel<T> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(null);
        if (items != null) {
            for (T item : items) {
                modelo.addElement(item);
            }
        }
        JComboBox<T> combo = new JComboBox<>(modelo);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? textoVacio : value.toString());
                return this;
            }
        });
        return combo;
    }

    public static <T> void recargarCombo(JComboBox<T> combo, List<T> items, T seleccionado) {
        DefaultComboBoxModel<T> modelo = new DefaultComboBoxModel<>();
        modelo.addElement(null);
        if (items != null) {
            for (T item : items) {
                modelo.addElement(item);
            }
        }
        combo.setModel(modelo);
        combo.setSelectedItem(seleccionado);
    }

    public static void abrirEnEscritorio(Component invocador, JInternalFrame nuevo) {
        JDesktopPane desktop = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, invocador);
        if (desktop == null) {
            return;
        }
        for (JInternalFrame frame : desktop.getAllFrames()) {
            if (frame.getClass().equals(nuevo.getClass())) {
                try {
                    frame.setSelected(true);
                    frame.toFront();
                } catch (Exception ignored) {
                }
                return;
            }
        }
        desktop.add(nuevo);
        nuevo.setVisible(true);
        try {
            nuevo.setSelected(true);
        } catch (Exception ignored) {
        }
    }
}
