package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Pedido;
import java.util.List;

public interface PedidoDAO {
    void guardar(Pedido pedido);
    List<Pedido> listar();
    Pedido buscarPorId(int id);
    List<Pedido> buscar(Integer clienteId, String estado);
    void actualizar(Pedido pedido);
    void eliminar(int id);
}
