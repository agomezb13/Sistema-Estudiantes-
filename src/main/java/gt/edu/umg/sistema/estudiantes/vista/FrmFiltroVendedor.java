package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.VendedorController;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import javax.swing.JTextField;

public class FrmFiltroVendedor extends FrmFiltroBase {

    private final VendedorController controller;
    private final JTextField txtNombre = new JTextField(18);
    private final JTextField txtCorreo = new JTextField(18);

    public FrmFiltroVendedor(VendedorController controller) {
        super("Vendedores - Consulta y Filtro", new String[]{"Id", "Nombre", "Correo", "Teléfono", "Estado"});
        this.controller = controller;
        FormularioHelper.agregarCampo(panelFiltros, 0, "Nombre:", txtNombre);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Correo:", txtCorreo);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        for (Vendedor v : controller.buscar(FormularioHelper.texto(txtNombre), FormularioHelper.texto(txtCorreo))) {
            modelo.addRow(new Object[]{
                v.getId(),
                FormularioHelper.textoSeguro(v.getNombre()),
                FormularioHelper.textoSeguro(v.getCorreo()),
                FormularioHelper.textoSeguro(v.getTelefono()),
                FormularioHelper.textoSeguro(v.getEstado())
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        txtNombre.setText("");
        txtCorreo.setText("");
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoVendedor(controller, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoVendedor(controller, controller.buscarPorId(id), this::buscar));
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
