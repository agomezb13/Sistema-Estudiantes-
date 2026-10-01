package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.PagoDAO;
import gt.edu.umg.sistema.estudiantes.dao.PagoDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import java.util.List;

public class PagoController {

    private final PagoDAO dao;

    public PagoController() {
        this.dao = new PagoDAOImpl();
    }

    public PagoController(PagoDAO dao) {
        this.dao = dao;
    }

    public void guardar(Pago pago) {
        if (pago.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero.");
        }
        dao.guardar(pago);
    }

    public List<Pago> listar() {
        return dao.listar();
    }

    public Pago buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public Pago buscarPorPedido(int pedidoId) {
        return dao.buscarPorPedido(pedidoId);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
