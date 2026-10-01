package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Pago;
import java.util.List;

public interface PagoDAO {
    void guardar(Pago pago);
    List<Pago> listar();
    Pago buscarPorId(int id);
    Pago buscarPorPedido(int pedidoId);
    void eliminar(int id);
}
