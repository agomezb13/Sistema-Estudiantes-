package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAOImpl implements VendedorDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Vendedor vendedor) {
        if (vendedor.getId() == 0) {
            vendedor.setId(db.siguienteIdVendedor());
            db.getVendedores().add(vendedor);
        } else {
            actualizar(vendedor);
        }
    }

    @Override
    public List<Vendedor> listar() {
        return new ArrayList<>(db.getVendedores());
    }

    @Override
    public Vendedor buscarPorId(int id) {
        for (Vendedor v : db.getVendedores()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    @Override
    public List<Vendedor> buscar(String nombre, String correo) {
        List<Vendedor> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        String cLower = correo == null ? "" : correo.toLowerCase();

        for (Vendedor v : db.getVendedores()) {
            boolean coincideNom = nLower.isEmpty() || (v.getNombre() != null && v.getNombre().toLowerCase().contains(nLower));
            boolean coincideCor = cLower.isEmpty() || (v.getCorreo() != null && v.getCorreo().toLowerCase().contains(cLower));
            if (coincideNom && coincideCor) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Vendedor vendedor) {
        Vendedor actual = buscarPorId(vendedor.getId());
        if (actual == null) {
            db.getVendedores().add(vendedor);
            return;
        }
        actual.setNombre(vendedor.getNombre());
        actual.setCorreo(vendedor.getCorreo());
        actual.setTelefono(vendedor.getTelefono());
        actual.setEstado(vendedor.getEstado());
    }

    @Override
    public void eliminar(int id) {
        db.getVendedores().removeIf(v -> v.getId() == id);
    }
}
