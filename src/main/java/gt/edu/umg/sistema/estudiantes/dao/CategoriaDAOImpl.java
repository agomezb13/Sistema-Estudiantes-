package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOImpl implements CategoriaDAO {

    @Override
    public void guardar(Categoria categoria) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            if (categoria.getId() > 0) {
                if (buscarPorId(categoria.getId()) != null) {
                    actualizar(categoria);
                    return;
                }
                String sql = "INSERT INTO categoria (id, nombre, descripcion) VALUES (?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, categoria.getId());
                    ps.setString(2, categoria.getNombre());
                    ps.setString(3, categoria.getDescripcion() == null ? "" : categoria.getDescripcion());
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, categoria.getNombre());
                    ps.setString(2, categoria.getDescripcion() == null ? "" : categoria.getDescripcion());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            categoria.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, nombre, descripcion FROM categoria ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public Categoria buscarPorId(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return null;
        }

        String sql = "SELECT id, nombre, descripcion FROM categoria WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.buscarPorId: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return null;
    }

    @Override
    public List<Categoria> buscar(String nombre) {
        List<Categoria> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, nombre, descripcion FROM categoria WHERE ? = '' OR LOWER(nombre) LIKE ? ORDER BY id ASC";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            String val = (nombre == null) ? "" : nombre.trim().toLowerCase();
            ps.setString(1, val);
            ps.setString(2, "%" + val + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.buscar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Categoria categoria) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion() == null ? "" : categoria.getDescripcion());
            ps.setInt(3, categoria.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.actualizar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public void eliminar(int id) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "DELETE FROM categoria WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error CategoriaDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
