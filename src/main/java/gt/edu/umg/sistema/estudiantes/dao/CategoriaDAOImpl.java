package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Categoria categoria) {
        if (categoria.getId() == 0) {
            categoria.setId(db.siguienteIdCategoria());
            db.getCategorias().add(categoria);
        } else {
            actualizar(categoria);
        }
    }

    @Override
    public List<Categoria> listar() {
        return new ArrayList<>(db.getCategorias());
    }

    @Override
    public Categoria buscarPorId(int id) {
        for (Categoria c : db.getCategorias()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Categoria> buscar(String nombre) {
        List<Categoria> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        for (Categoria c : db.getCategorias()) {
            if (nLower.isEmpty() || (c.getNombre() != null && c.getNombre().toLowerCase().contains(nLower))) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Categoria categoria) {
        Categoria actual = buscarPorId(categoria.getId());
        if (actual == null) {
            db.getCategorias().add(categoria);
            return;
        }
        actual.setNombre(categoria.getNombre());
        actual.setDescripcion(categoria.getDescripcion());
    }

    @Override
    public void eliminar(int id) {
        db.getCategorias().removeIf(c -> c.getId() == id);
    }
}
