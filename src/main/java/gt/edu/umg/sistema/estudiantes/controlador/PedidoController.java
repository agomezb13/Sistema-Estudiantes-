package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.PedidoDAO;
import gt.edu.umg.sistema.estudiantes.dao.PedidoDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.util.List;

public class PedidoController {

    private final PedidoDAO dao;

    public PedidoController() {
        this.dao = new PedidoDAOImpl();
    }

    public PedidoController(PedidoDAO dao) {
        this.dao = dao;
    }

    public void guardar(Pedido pedido) {
        if (pedido.getClienteId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente para el pedido.");
        }
        pedido.calcularTotal();
        dao.guardar(pedido);
    }

    public List<Pedido> listar() {
        return dao.listar();
    }

    public Pedido buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Pedido> buscar(Integer clienteId, String estado) {
        return dao.buscar(clienteId, estado);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
