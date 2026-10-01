package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.PagoController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import javax.swing.JComboBox;

public class FrmFiltroPago extends FrmFiltroBase {

    private final PagoController pagoController;
    private final PedidoController pedidoController;

    private final JComboBox<Pedido> cmbPedido;
    private final JComboBox<String> cmbMetodo;

    public FrmFiltroPago(PagoController pagoController, PedidoController pedidoController) {
        super("Pagos - Consulta y Filtro", new String[]{"Id", "No. Pedido", "Monto (Q)", "Método", "Estado"});
        this.pagoController = pagoController;
        this.pedidoController = pedidoController;

        this.cmbPedido = FormularioHelper.comboConOpcionVacia(pedidoController.listar(), "-- Todos los pedidos --");
        this.cmbMetodo = new JComboBox<>(new String[]{"-- Todos --", "EFECTIVO", "TARJETA", "TRANSFERENCIA", "CHEQUE"});

        FormularioHelper.agregarCampo(panelFiltros, 0, "Pedido:", cmbPedido);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Método:", cmbMetodo);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        Pedido ped = (Pedido) cmbPedido.getSelectedItem();
        Integer pedId = ped != null ? ped.getId() : null;

        String met = (String) cmbMetodo.getSelectedItem();
        String metodoFiltro = (met == null || met.startsWith("--")) ? "" : met;

        for (Pago p : pagoController.listar()) {
            if (pedId != null && p.getPedidoId() != pedId) {
                continue;
            }
            if (!metodoFiltro.isEmpty() && !metodoFiltro.equalsIgnoreCase(p.getMetodo())) {
                continue;
            }

            modelo.addRow(new Object[]{
                p.getId(),
                "Pedido #" + p.getPedidoId(),
                String.format("%.2f", p.getMonto()),
                p.getMetodo(),
                p.getEstado()
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        cmbPedido.setSelectedItem(null);
        cmbMetodo.setSelectedIndex(0);
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoPago(pagoController, pedidoController, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoPago(pagoController, pedidoController, pagoController.buscarPorId(id), this::buscar));
    }

    @Override
    protected void eliminar() {
        int id = idSeleccionadoInt();
        if (id < 0 || !confirmarEliminar()) {
            return;
        }
        pagoController.eliminar(id);
        buscar();
    }
}
