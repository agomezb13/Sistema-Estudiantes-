package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import javax.swing.JTextField;

public class FrmFiltroCliente extends FrmFiltroBase {

    private final ClienteController controller;
    private final JTextField txtNit = new JTextField(12);
    private final JTextField txtNombre = new JTextField(18);

    public FrmFiltroCliente(ClienteController controller) {
        super("Clientes - Consulta y Filtro", new String[]{"Id", "Nombre", "Correo", "Teléfono", "Dirección", "DPI", "NIT", "Estado"});
        this.controller = controller;
        FormularioHelper.agregarCampo(panelFiltros, 0, "NIT:", txtNit);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Nombre:", txtNombre);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        for (Cliente c : controller.buscar(FormularioHelper.texto(txtNit), FormularioHelper.texto(txtNombre))) {
            modelo.addRow(new Object[]{
                c.getId(),
                FormularioHelper.textoSeguro(c.getNombre()),
                FormularioHelper.textoSeguro(c.getCorreo()),
                FormularioHelper.textoSeguro(c.getTelefono()),
                FormularioHelper.textoSeguro(c.getDireccion()),
                FormularioHelper.textoSeguro(c.getNumeroDPI()),
                FormularioHelper.textoSeguro(c.getNIT()),
                FormularioHelper.textoSeguro(c.getEstado())
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        txtNit.setText("");
        txtNombre.setText("");
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoCliente(controller, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoCliente(controller, controller.buscarPorId(id), this::buscar));
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
