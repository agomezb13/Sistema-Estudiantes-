package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.modelo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOImpl implements EstudianteDAO {

    @Override
    public void guardar(Estudiante estudiante) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        try {
            if (estudiante.getId() > 0) {
                boolean existe = false;
                String checkSql = "SELECT id FROM estudiante WHERE id = ?";
                try (PreparedStatement psCheck = cn.prepareStatement(checkSql)) {
                    psCheck.setInt(1, estudiante.getId());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        existe = rs.next();
                    }
                }
                if (existe) {
                    actualizar(estudiante);
                    return;
                }
                String sql = "INSERT INTO estudiante (id, carnet, nombres, apellidos, email, correo, telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, estudiante.getId());
                    ps.setString(2, estudiante.getCarnet() == null ? "" : estudiante.getCarnet());
                    ps.setString(3, estudiante.getNombres() == null ? "" : estudiante.getNombres());
                    ps.setString(4, estudiante.getApellidos() == null ? "" : estudiante.getApellidos());
                    ps.setString(5, estudiante.getEmail() == null ? "" : estudiante.getEmail());
                    ps.setString(6, estudiante.getEmail() == null ? "" : estudiante.getEmail());
                    ps.setString(7, "");
                    ps.executeUpdate();
                }
            } else {
                String sql = "INSERT INTO estudiante (carnet, nombres, apellidos, email, correo, telefono) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, estudiante.getCarnet() == null ? "" : estudiante.getCarnet());
                    ps.setString(2, estudiante.getNombres() == null ? "" : estudiante.getNombres());
                    ps.setString(3, estudiante.getApellidos() == null ? "" : estudiante.getApellidos());
                    ps.setString(4, estudiante.getEmail() == null ? "" : estudiante.getEmail());
                    ps.setString(5, estudiante.getEmail() == null ? "" : estudiante.getEmail());
                    ps.setString(6, "");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            estudiante.setId(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error EstudianteDAOImpl.guardar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    @Override
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id, carnet, nombres, apellidos, email FROM estudiante ORDER BY id ASC";
        try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setId(rs.getInt("id"));
                e.setCarnet(rs.getString("carnet"));
                e.setNombres(rs.getString("nombres"));
                e.setApellidos(rs.getString("apellidos"));
                e.setEmail(rs.getString("email"));
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error EstudianteDAOImpl.listar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
        return lista;
    }

    @Override
    public void actualizar(Estudiante estudiante) {
        Connection cn = ConexionMySQL.getConnection();
        if (cn == null) {
            return;
        }

        String sql = "UPDATE estudiante SET carnet = ?, nombres = ?, apellidos = ?, email = ?, correo = ? WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, estudiante.getCarnet() == null ? "" : estudiante.getCarnet());
            ps.setString(2, estudiante.getNombres() == null ? "" : estudiante.getNombres());
            ps.setString(3, estudiante.getApellidos() == null ? "" : estudiante.getApellidos());
            ps.setString(4, estudiante.getEmail() == null ? "" : estudiante.getEmail());
            ps.setString(5, estudiante.getEmail() == null ? "" : estudiante.getEmail());
            ps.setInt(6, estudiante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error EstudianteDAOImpl.actualizar: " + e.getMessage());
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

        String sql = "DELETE FROM estudiante WHERE id = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error EstudianteDAOImpl.eliminar: " + e.getMessage());
        } finally {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }
}
