package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import java.util.ArrayList;
import java.util.List;

public class PagoDAOImpl implements PagoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Pago pago) {
        if (pago.getId() == 0) {
            pago.setId(db.siguienteIdPago());
            db.getPagos().add(pago);
        } else {
            Pago actual = buscarPorId(pago.getId());
            if (actual != null) {
                actual.setMonto(pago.getMonto());
                actual.setMetodo(pago.getMetodo());
                actual.setEstado(pago.getEstado());
            } else {
                db.getPagos().add(pago);
            }
        }
    }

    @Override
    public List<Pago> listar() {
        return new ArrayList<>(db.getPagos());
    }

    @Override
    public Pago buscarPorId(int id) {
        for (Pago p : db.getPagos()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public Pago buscarPorPedido(int pedidoId) {
        for (Pago p : db.getPagos()) {
            if (p.getPedidoId() == pedidoId) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void eliminar(int id) {
        db.getPagos().removeIf(p -> p.getId() == id);
    }
}
