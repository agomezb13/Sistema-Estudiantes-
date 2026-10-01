package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAOImpl implements PedidoDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Pedido pedido) {
        if (pedido.getId() == 0) {
            pedido.setId(db.siguienteIdPedido());
            db.getPedidos().add(pedido);
        } else {
            actualizar(pedido);
        }
    }

    @Override
    public List<Pedido> listar() {
        return new ArrayList<>(db.getPedidos());
    }

    @Override
    public Pedido buscarPorId(int id) {
        for (Pedido p : db.getPedidos()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Pedido> buscar(Integer clienteId, String estado) {
        List<Pedido> res = new ArrayList<>();
        String estLower = estado == null ? "" : estado.toLowerCase();
        for (Pedido p : db.getPedidos()) {
            boolean coincideCliente = clienteId == null || p.getClienteId() == clienteId;
            boolean coincideEstado = estLower.isEmpty() || (p.getEstado() != null && p.getEstado().toLowerCase().contains(estLower));
            if (coincideCliente && coincideEstado) {
                res.add(p);
            }
        }
        return res;
    }

    @Override
    public void actualizar(Pedido pedido) {
        Pedido actual = buscarPorId(pedido.getId());
        if (actual == null) {
            db.getPedidos().add(pedido);
            return;
        }
        actual.setClienteId(pedido.getClienteId());
        actual.setDireccionEnvioId(pedido.getDireccionEnvioId());
        actual.setFecha(pedido.getFecha());
        actual.setEstado(pedido.getEstado());
        actual.setTotal(pedido.getTotal());
        actual.setDetalles(pedido.getDetalles());
    }

    @Override
    public void eliminar(int id) {
        db.getPedidos().removeIf(p -> p.getId() == id);
    }
}
