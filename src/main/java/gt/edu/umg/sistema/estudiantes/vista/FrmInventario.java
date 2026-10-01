package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.controlador.InventarioController;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class FrmInventario extends JInternalFrame {

    private final InventarioController inventarioController;
    private final CategoriaController categoriaController;
    private final DefaultTableModel modelo;
    private final JTable tabla;

    private final JTextField txtBuscar = new JTextField(16);
    private final JComboBox<Categoria> cmbCategoria;

    public FrmInventario(InventarioController inventarioController, CategoriaController categoriaController) {
        super("Inventario - Consulta y Control de Stock", true, true, true, true);
        this.inventarioController = inventarioController;
        this.categoriaController = categoriaController;
        this.cmbCategoria = FormularioHelper.comboConOpcionVacia(categoriaController.listar(), "-- Todas las categorías --");

        setSize(880, 520);
        setLocation(30, 30);

        String[] columnas = {"Id", "Producto", "Categoría", "Precio (Q)", "Existencias", "Estado de Stock"};
        modelo = FormularioHelper.modeloNoEditable(columnas);
        tabla = new JTable(modelo);

        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Búsqueda y Filtros de Inventario"));
        FormularioHelper.agregarCampo(panelFiltros, 0, "Buscar Producto:", txtBuscar);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Filtrar por Categoría:", cmbCategoria);

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar Filtros");
        JButton btnVerDetalle = new JButton("Ver Detalle Producto");
        JButton btnActualizarStock = new JButton("Actualizar Stock");

        btnBuscar.addActionListener(e -> cargarInventario());
        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            cmbCategoria.setSelectedItem(null);
            cargarInventario();
        });
        btnVerDetalle.addActionListener(e -> verDetalle());
        btnActualizarStock.addActionListener(e -> actualizarStock());

        JPanel accionesFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        accionesFiltro.add(btnBuscar);
        accionesFiltro.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout(0, 6));
        panelNorte.add(panelFiltros, BorderLayout.CENTER);
        panelNorte.add(accionesFiltro, BorderLayout.SOUTH);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelAcciones.add(btnVerDetalle);
        panelAcciones.add(btnActualizarStock);

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.add(panelAcciones, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(panelNorte, BorderLayout.NORTH);
        getContentPane().add(centro, BorderLayout.CENTER);

        cargarInventario();
    }

    private void cargarInventario() {
        modelo.setRowCount(0);
        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
        Integer catId = cat != null ? cat.getId() : null;

        String query = FormularioHelper.texto(txtBuscar).toLowerCase();
        for (Producto p : inventarioController.filtrar(catId)) {
            if (!query.isEmpty() && !p.getNombre().toLowerCase().contains(query)) {
                continue;
            }
            Categoria c = categoriaController.buscarPorId(p.getCategoriaId());
            String nomCat = c != null ? c.getNombre() : "Sin Categoría";
            String estadoStock = p.getExistencias() <= 5 ? "BAJO STOCK (" + p.getExistencias() + ")" : "NORMAL (" + p.getExistencias() + ")";

            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                nomCat,
                String.format("%.2f", p.getPrecio()),
                p.getExistencias(),
                estadoStock
            });
        }
    }

    private void verDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto del inventario.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0)));
        Producto p = inventarioController.verDetalle(id);
        if (p != null) {
            Categoria c = categoriaController.buscarPorId(p.getCategoriaId());
            String msg = "Detalle del Producto:\n\n"
                    + "ID: " + p.getId() + "\n"
                    + "Nombre: " + p.getNombre() + "\n"
                    + "Categoría: " + (c != null ? c.getNombre() : "N/A") + "\n"
                    + "Precio: Q. " + String.format("%.2f", p.getPrecio()) + "\n"
                    + "Existencias: " + p.getExistencias() + "\n"
                    + "Descripción: " + p.getDescripcion();
            JOptionPane.showMessageDialog(this, msg, "Detalle de Producto", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarStock() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar su stock.");
            return;
        }
        int id = Integer.parseInt(String.valueOf(modelo.getValueAt(tabla.convertRowIndexToModel(fila), 0)));
        Producto p = inventarioController.verDetalle(id);
        if (p == null) {
            return;
        }

        String input = JOptionPane.showInputDialog(this, "Ingrese la nueva cantidad de existencias para " + p.getNombre() + ":", String.valueOf(p.getExistencias()));
        if (input != null && !input.trim().isEmpty()) {
            try {
                int nuevo = Integer.parseInt(input.trim());
                if (nuevo < 0) {
                    JOptionPane.showMessageDialog(this, "El stock no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                inventarioController.actualizarStock(id, nuevo);
                cargarInventario();
                JOptionPane.showMessageDialog(this, "Stock actualizado exitosamente.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
