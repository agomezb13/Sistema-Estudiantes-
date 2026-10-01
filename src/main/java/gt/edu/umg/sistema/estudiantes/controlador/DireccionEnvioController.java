package gt.edu.umg.sistema.estudiantes.controlador;

import gt.edu.umg.sistema.estudiantes.dao.DireccionEnvioDAO;
import gt.edu.umg.sistema.estudiantes.dao.DireccionEnvioDAOImpl;
import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import java.util.List;

public class DireccionEnvioController {

    private final DireccionEnvioDAO dao;

    public DireccionEnvioController() {
        this.dao = new DireccionEnvioDAOImpl();
    }

    public DireccionEnvioController(DireccionEnvioDAO dao) {
        this.dao = dao;
    }

    public void guardar(DireccionEnvio direccion) {
        dao.guardar(direccion);
    }

    public List<DireccionEnvio> listar() {
        return dao.listar();
    }

    public List<DireccionEnvio> listarPorCliente(int clienteId) {
        return dao.listarPorCliente(clienteId);
    }

    public DireccionEnvio buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public void eliminar(int id) {
        dao.eliminar(id);
    }
}
