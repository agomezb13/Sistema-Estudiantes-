package gt.edu.umg.sistema.estudiantes.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public abstract class FrmFiltroBase extends JInternalFrame {

    protected final DefaultTableModel modelo;
    protected final JTable tabla;
    protected final JPanel panelFiltros;

    protected FrmFiltroBase(String titulo, String[] columnas) {
        super(titulo, true, true, true, true);
        setSize(860, 540);
        setLocation(20, 20);

        modelo = FormularioHelper.modeloNoEditable(columnas);
        tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editar();
                }
            }
        });

        panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de búsqueda"));

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> {
            limpiarFiltros();
            buscar();
        });
        btnNuevo.addActionListener(e -> nuevo());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());

        JPanel panelAccionesFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAccionesFiltro.add(btnBuscar);
        panelAccionesFiltro.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout(0, 6));
        panelNorte.add(panelFiltros, BorderLayout.CENTER);
        panelNorte.add(panelAccionesFiltro, BorderLayout.SOUTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.add(btnNuevo);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnEliminar);

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.add(panelAcciones, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(centro, BorderLayout.CENTER);
    }

    protected abstract void buscar();
    protected abstract void limpiarFiltros();
    protected abstract void nuevo();
    protected abstract void editar();
    protected abstract void eliminar();

    protected int idSeleccionadoInt() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Por favor seleccione un registro de la tabla.");
            return -1;
        }
        int modeloFila = tabla.convertRowIndexToModel(fila);
        return Integer.parseInt(String.valueOf(modelo.getValueAt(modeloFila, 0)));
    }

    protected boolean confirmarEliminar() {
        return JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el registro seleccionado?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}
