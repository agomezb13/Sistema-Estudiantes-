package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Vendedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "INSERT INTO vendedor (id, nombre, codigo_empleado, departamento, telefono, correo, estado) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), departamento=VALUES(departamento), "
                    + "telefono=VALUES(telefono), correo=VALUES(correo), estado=VALUES(estado)";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, vendedor.getId());
                ps.setString(2, vendedor.getNombre());
                ps.setString(3, "VEND-" + String.format("%03d", vendedor.getId()));
                ps.setString(4, "Ventas");
                ps.setString(5, vendedor.getTelefono() == null ? "" : vendedor.getTelefono());
                ps.setString(6, vendedor.getCorreo() == null ? "" : vendedor.getCorreo());
                ps.setString(7, vendedor.getEstado() == null ? "ACTIVO" : vendedor.getEstado());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al persistir vendedor en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    @Override
    public List<Vendedor> listar() {
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, nombre, telefono, correo, estado FROM vendedor ORDER BY id ASC";
            try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                List<Vendedor> desdeBD = new ArrayList<>();
                while (rs.next()) {
                    desdeBD.add(new Vendedor(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("correo"),
                            rs.getString("telefono"),
                            rs.getString("estado")
                    ));
                }
                if (!desdeBD.isEmpty()) {
                    db.getVendedores().clear();
                    db.getVendedores().addAll(desdeBD);
                    return desdeBD;
                }
            } catch (SQLException e) {
                System.out.println("Aviso al listar vendedores de BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return new ArrayList<>(db.getVendedores());
    }

    @Override
    public Vendedor buscarPorId(int id) {
        for (Vendedor v : listar()) {
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

        for (Vendedor v : listar()) {
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
        Vendedor actual = null;
        for (Vendedor v : db.getVendedores()) {
            if (v.getId() == vendedor.getId()) {
                actual = v;
                break;
            }
        }
        if (actual == null) {
            db.getVendedores().add(vendedor);
        } else {
            actual.setNombre(vendedor.getNombre());
            actual.setCorreo(vendedor.getCorreo());
            actual.setTelefono(vendedor.getTelefono());
            actual.setEstado(vendedor.getEstado());
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "UPDATE vendedor SET nombre = ?, telefono = ?, correo = ?, estado = ? WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, vendedor.getNombre());
                ps.setString(2, vendedor.getTelefono() == null ? "" : vendedor.getTelefono());
                ps.setString(3, vendedor.getCorreo() == null ? "" : vendedor.getCorreo());
                ps.setString(4, vendedor.getEstado() == null ? "ACTIVO" : vendedor.getEstado());
                ps.setInt(5, vendedor.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al actualizar vendedor en BD: " + e.getMessage());
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
        db.getVendedores().removeIf(v -> v.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM vendedor WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar vendedor en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
