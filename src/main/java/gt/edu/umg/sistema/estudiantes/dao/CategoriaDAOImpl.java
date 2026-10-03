package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "INSERT INTO categoria (id, nombre, descripcion) VALUES (?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), descripcion=VALUES(descripcion)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, categoria.getId());
                ps.setString(2, categoria.getNombre());
                ps.setString(3, categoria.getDescripcion() == null ? "" : categoria.getDescripcion());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al persistir categoria en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Categoria> listar() {
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, nombre, descripcion FROM categoria ORDER BY id ASC";
            try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                List<Categoria> desdeBD = new ArrayList<>();
                while (rs.next()) {
                    desdeBD.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
                }
                if (!desdeBD.isEmpty()) {
                    db.getCategorias().clear();
                    db.getCategorias().addAll(desdeBD);
                    return desdeBD;
                }
            } catch (SQLException e) {
                System.out.println("Aviso al listar categorias de BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return new ArrayList<>(db.getCategorias());
    }

    @Override
    public Categoria buscarPorId(int id) {
        for (Categoria c : listar()) {
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
        for (Categoria c : listar()) {
            if (nLower.isEmpty() || (c.getNombre() != null && c.getNombre().toLowerCase().contains(nLower))) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public void actualizar(Categoria categoria) {
        Categoria actual = null;
        for (Categoria c : db.getCategorias()) {
            if (c.getId() == categoria.getId()) {
                actual = c;
                break;
            }
        }
        if (actual == null) {
            db.getCategorias().add(categoria);
        } else {
            actual.setNombre(categoria.getNombre());
            actual.setDescripcion(categoria.getDescripcion());
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, categoria.getNombre());
                ps.setString(2, categoria.getDescripcion() == null ? "" : categoria.getDescripcion());
                ps.setInt(3, categoria.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al actualizar categoria en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public void eliminar(int id) {
        db.getCategorias().removeIf(c -> c.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM categoria WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar categoria en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
