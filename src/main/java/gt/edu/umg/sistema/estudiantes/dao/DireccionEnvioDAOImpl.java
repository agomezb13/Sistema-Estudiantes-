package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.DireccionEnvio;
import java.util.ArrayList;
import java.util.List;

public class DireccionEnvioDAOImpl implements DireccionEnvioDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(DireccionEnvio direccion) {
        if (direccion.getId() == 0) {
            direccion.setId(db.siguienteIdDireccion());
            db.getDirecciones().add(direccion);
        } else {
            DireccionEnvio actual = buscarPorId(direccion.getId());
            if (actual != null) {
                actual.setCalle(direccion.getCalle());
                actual.setCiudad(direccion.getCiudad());
                actual.setCodigoPostal(direccion.getCodigoPostal());
                actual.setPais(direccion.getPais());
            } else {
                db.getDirecciones().add(direccion);
            }
        }
    }

    @Override
    public List<DireccionEnvio> listar() {
        return new ArrayList<>(db.getDirecciones());
    }

    @Override
    public List<DireccionEnvio> listarPorCliente(int clienteId) {
        List<DireccionEnvio> res = new ArrayList<>();
        for (DireccionEnvio d : db.getDirecciones()) {
            if (d.getClienteId() == clienteId) {
                res.add(d);
            }
        }
        return res;
    }

    @Override
    public DireccionEnvio buscarPorId(int id) {
        for (DireccionEnvio d : db.getDirecciones()) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    @Override
    public void eliminar(int id) {
        db.getDirecciones().removeIf(d -> d.getId() == id);
    }
}
