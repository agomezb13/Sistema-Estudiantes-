package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import gt.edu.umg.sistema.estudiantes.modelo.Producto;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class FrmFiltroProducto extends FrmFiltroBase {

    private final ProductoController productoController;
    private final CategoriaController categoriaController;
    private final JTextField txtNombre = new JTextField(18);
    private final JComboBox<Categoria> cmbCategoria;

    public FrmFiltroProducto(ProductoController productoController, CategoriaController categoriaController) {
        super("Productos - Consulta y Filtro", new String[]{"Id", "Nombre", "Categoría", "Precio (Q)", "Existencias", "Descripción"});
        this.productoController = productoController;
        this.categoriaController = categoriaController;
        this.cmbCategoria = FormularioHelper.comboConOpcionVacia(categoriaController.listar(), "-- Todas las categorías --");

        FormularioHelper.agregarCampo(panelFiltros, 0, "Nombre:", txtNombre);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Categoría:", cmbCategoria);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        Categoria cat = (Categoria) cmbCategoria.getSelectedItem();
        Integer catId = cat != null ? cat.getId() : null;

        for (Producto p : productoController.buscar(FormularioHelper.texto(txtNombre), catId)) {
            Categoria c = categoriaController.buscarPorId(p.getCategoriaId());
            String nombreCat = c != null ? c.getNombre() : "Sin Categoría";
            modelo.addRow(new Object[]{
                p.getId(),
                FormularioHelper.textoSeguro(p.getNombre()),
                nombreCat,
                String.format("%.2f", p.getPrecio()),
                p.getExistencias(),
                FormularioHelper.textoSeguro(p.getDescripcion())
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        txtNombre.setText("");
        cmbCategoria.setSelectedItem(null);
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoProducto(productoController, categoriaController, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoProducto(productoController, categoriaController, productoController.buscarPorId(id), this::buscar));
    }

    @Override
    protected void eliminar() {
        int id = idSeleccionadoInt();
        if (id < 0 || !confirmarEliminar()) {
            return;
        }
        productoController.eliminar(id);
        buscar();
    }
}
