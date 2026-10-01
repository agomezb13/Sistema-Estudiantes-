package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import java.util.List;

public interface DireccionEnvioDAO {
    void guardar(DireccionEnvio direccion);
    List<DireccionEnvio> listar();
    List<DireccionEnvio> listarPorCliente(int clienteId);
    DireccionEnvio buscarPorId(int id);
    void eliminar(int id);
}
