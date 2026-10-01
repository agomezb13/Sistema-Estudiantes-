package gt.edu.umg.sistema.estudiantes.vista;

import gt.edu.umg.sistema.estudiantes.controlador.ClienteController;
import gt.edu.umg.sistema.estudiantes.controlador.FacturaController;
import gt.edu.umg.sistema.estudiantes.controlador.PedidoController;
import gt.edu.umg.sistema.estudiantes.controlador.ProductoController;
import gt.edu.umg.sistema.estudiantes.controlador.VendedorController;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class FrmFiltroFactura extends FrmFiltroBase {

    private final FacturaController facturaController;
    private final ClienteController clienteController;
    private final VendedorController vendedorController;
    private final PedidoController pedidoController;
    private final ProductoController productoController;

    private final JTextField txtNumero = new JTextField(15);
    private final JComboBox<Cliente> cmbCliente;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmFiltroFactura(FacturaController facturaController, ClienteController clienteController,
                            VendedorController vendedorController, PedidoController pedidoController,
                            ProductoController productoController) {
        super("Facturas - Consulta y Filtro",
                new String[]{"Id", "No. Factura", "Fecha Emisión", "Cliente", "Vendedor", "Subtotal (Q)", "IVA (Q)", "Total (Q)", "Estado"});
        this.facturaController = facturaController;
        this.clienteController = clienteController;
        this.vendedorController = vendedorController;
        this.pedidoController = pedidoController;
        this.productoController = productoController;

        this.cmbCliente = FormularioHelper.comboConOpcionVacia(clienteController.listar(), "-- Todos los clientes --");

        FormularioHelper.agregarCampo(panelFiltros, 0, "No. Factura:", txtNumero);
        FormularioHelper.agregarCampo(panelFiltros, 1, "Cliente:", cmbCliente);
        buscar();
    }

    @Override
    protected void buscar() {
        modelo.setRowCount(0);
        String num = txtNumero.getText().trim();
        Cliente cli = (Cliente) cmbCliente.getSelectedItem();
        Integer cliId = cli != null ? cli.getId() : null;

        for (Factura f : facturaController.buscar(num, cliId)) {
            Cliente c = clienteController.buscarPorId(f.getClienteId());
            String nomCliente = c != null ? c.getNombre() : "Cliente #" + f.getClienteId();

            Vendedor v = vendedorController.buscarPorId(f.getVendedorId());
            String nomVendedor = v != null ? v.getNombre() : "Vendedor #" + f.getVendedorId();

            String fechaStr = f.getFechaEmision() != null ? sdf.format(f.getFechaEmision()) : "";

            modelo.addRow(new Object[]{
                f.getId(),
                f.getNumero(),
                fechaStr,
                nomCliente,
                nomVendedor,
                String.format("%.2f", f.getSubtotal()),
                String.format("%.2f", f.getImpuesto()),
                String.format("%.2f", f.getTotal()),
                f.getEstado()
            });
        }
    }

    @Override
    protected void limpiarFiltros() {
        txtNumero.setText("");
        cmbCliente.setSelectedItem(null);
    }

    @Override
    protected void nuevo() {
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoFactura(
                facturaController, clienteController, vendedorController, pedidoController, productoController, null, this::buscar
        ));
    }

    @Override
    protected void editar() {
        int id = idSeleccionadoInt();
        if (id < 0) {
            return;
        }
        FormularioHelper.abrirEnEscritorio(this, new FrmTecleoFactura(
                facturaController, clienteController, vendedorController, pedidoController, productoController, facturaController.buscarPorId(id), this::buscar
        ));
    }

    @Override
    protected void eliminar() {
        int id = idSeleccionadoInt();
        if (id < 0 || !confirmarEliminar()) {
            return;
        }
        facturaController.eliminar(id);
        buscar();
    }
}
