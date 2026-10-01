package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.FacturaDAO;
import gt.edu.umg.sistema.estudiantes.dao.FacturaDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import java.util.List;

public class FacturaController {

    private final FacturaDAO dao;

    public FacturaController() {
        this.dao = new FacturaDAOImpl();
    }

    public FacturaController(FacturaDAO dao) {
        this.dao = dao;
    }

    public void guardar(Factura factura) {
        if (factura.getNumero() == null || factura.getNumero().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio.");
        }
        if (factura.getClienteId() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un cliente para emitir la factura.");
        }
        factura.calcularTotal();
        dao.guardar(factura);
    }

    public List<Factura> listar() {
        return dao.listar();
    }

    public Factura buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public List<Factura> buscar(String numero, Integer clienteId) {
        return dao.buscar(numero, clienteId);
    }

    public void anular(int id) {
        dao.anular(id);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
