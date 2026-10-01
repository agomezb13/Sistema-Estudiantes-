package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.CategoriaController;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import javax.swing.JTextField;

public class FrmFiltroCategoria extends FrmFiltroBase {

    private final CategoriaController controller;
    private final JTextField txtNombre = new JTextField(18);

    public FrmFiltroCategoria(CategoriaController controller) {
        super("Categorías - Consulta y Filtro", new String[]{"Id", "Nombre", "Descripción"});
        this.controller = controller;
        FormularioHelper.agregarCampo(panelFiltros, 0, "Nombre:", txtNombre);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        for (Categoria c : controller.buscar(FormularioHelper.texto(txtNombre))) {
            modelo.addRow(new Object[]{
                c.getId(),
                FormularioHelper.textoSeguro(c.getNombre()),
                FormularioHelper.textoSeguro(c.getDescripcion())
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        txtNombre.setText("");
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoCategoria(controller, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoCategoria(controller, controller.buscarPorId(id), this::buscar));
    }

    @Override
    protected void eliminar() {
        int id = idSeleccionadoInt();
        if (id < 0 || !confirmarEliminar()) {
            return;
        }
        controller.eliminar(id);
        buscar();
    }
}
