package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.controlador.DireccionEnvioController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;

public class FrmFiltroPedido extends FrmFiltroBase {

    private final PedidoController pedidoController;
    private final ClienteController clienteController;
    private final ProductoController productoController;
    private final DireccionEnvioController direccionController;

    private final JComboBox<Cliente> cmbCliente;
    private final JComboBox<String> cmbEstado;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmFiltroPedido(PedidoController pedidoController, ClienteController clienteController, ProductoController productoController, DireccionEnvioController direccionController) {
        super("Pedidos - Consulta y Filtro", new String[]{"Id", "Cliente", "Fecha", "Estado", "Total (Q)"});
        this.pedidoController = pedidoController;
        this.clienteController = clienteController;
        this.productoController = productoController;
        this.direccionController = direccionController;

        this.cmbCliente = FormularioHelper.comboConOpcionVacia(clienteController.listar(), "-- Todos los clientes --");
        this.cmbEstado = new JComboBox<>(new String[]{"-- Todos --", "PENDIENTE", "CONFIRMADO", "CANCELADO"});

        FormularioHelper.agregarCampo(panelFiltros, 0, "Cliente:", cmbCliente);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Estado:", cmbEstado);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        Cliente cli = (Cliente) cmbCliente.getSelectedItem();
        Integer cliId = cli != null ? cli.getId() : null;

        String est = (String) cmbEstado.getSelectedItem();
        String estadoFiltro = (est == null || est.startsWith("--")) ? "" : est;

        for (Pedido p : pedidoController.buscar(cliId, estadoFiltro)) {
            Cliente c = clienteController.buscarPorId(p.getClienteId());
            String nomCliente = c != null ? c.getNombre() : "Cliente #" + p.getClienteId();
            String fechaStr = p.getFecha() != null ? sdf.format(p.getFecha()) : "";

            modelo.addRow(new Object[]{
                p.getId(),
                nomCliente,
                fechaStr,
                p.getEstado(),
                String.format("%.2f", p.getTotal())
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        cmbCliente.setSelectedItem(null);
        cmbEstado.setSelectedIndex(0);
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoPedido(pedidoController, clienteController, productoController, direccionController, null, this::buscar));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoPedido(pedidoController, clienteController, productoController, direccionController, pedidoController.buscarPorId(id), this::buscar));
    }

    @Override
    protected void eliminar() {
        int id = idSeleccionadoInt();
        if (id < 0 || !confirmarEliminar()) {
            return;
        }
        pedidoController.eliminar(id);
        buscar();
    }
}
