package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Cliente cliente) {
        if (cliente.getId() == 0) {
            cliente.setId(db.siguienteIdCliente());
            db.getClientes().add(cliente);
        } else {
            actualizar(cliente);
        }
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(db.getClientes());
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente c : db.getClientes()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> buscar(String nit, String nombre) {
        List<Cliente> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        String nitLower = nit == null ? "" : nit.toLowerCase();

        for (Cliente c : db.getClientes()) {
            boolean coincideNit = nitLower.isEmpty() || (c.getNIT() != null && c.getNIT().toLowerCase().contains(nitLower));
            boolean coincideNom = nLower.isEmpty() || (c.getNombre() != null && c.getNombre().toLowerCase().contains(nLower));
            if (coincideNit && coincideNom) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Cliente cliente) {
        Cliente actual = buscarPorId(cliente.getId());
        if (actual == null) {
            db.getClientes().add(cliente);
            return;
        }
        actual.setNombre(cliente.getNombre());
        actual.setCorreo(cliente.getCorreo());
        actual.setTelefono(cliente.getTelefono());
        actual.setDireccion(cliente.getDireccion());
        actual.setNumeroDPI(cliente.getNumeroDPI());
        actual.setNIT(cliente.getNIT());
        actual.setEstado(cliente.getEstado());
    }

    @Override
    public void eliminar(int id) {
        db.getClientes().removeIf(c -> c.getId() == id);
    }
}
