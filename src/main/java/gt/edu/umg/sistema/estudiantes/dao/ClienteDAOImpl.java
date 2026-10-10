package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import gt.edu.umg.sistema.estudiantes.datos.BaseDatosMemoria;
import gt.edu.umg.sistema.estudiantes.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion concreta del DAO para Cliente.
 * 
 * Responsabilidades:
 * - Realiza las consultas y actualizaciones JDBC hacia la base de datos MySQL (Oracle Cloud).
 * - Sincroniza los resultados con el cache local (BaseDatosMemoria).
 * - Maneja el cierre seguro de recursos JDBC (PreparedStatement, Connection).
 */
public class ClienteDAOImpl implements ClienteDAO {

    private final BaseDatosMemoria db = BaseDatosMemoria.getInstancia();

    @Override
    public void guardar(Cliente cliente) {
        boolean esNuevo = (cliente.getId() <= 0) || (buscarPorId(cliente.getId()) == null);

        if (cliente.getId() <= 0) {
            cliente.setId(db.siguienteIdCliente());
        }

        if (esNuevo) {
            db.getClientes().removeIf(c -> c.getId() == cliente.getId());
            db.getClientes().add(cliente);

            Connection cn = ConexionMySQL.getConnection();
            if (cn != null) {
                String sql = "INSERT INTO cliente (id, nombre, correo, telefono, direccion, numero_dpi, nit, estado) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                        + "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), correo=VALUES(correo), telefono=VALUES(telefono), "
                        + "direccion=VALUES(direccion), numero_dpi=VALUES(numero_dpi), nit=VALUES(nit), estado=VALUES(estado)";
                try (PreparedStatement ps = cn.prepareStatement(sql)) {
                    ps.setInt(1, cliente.getId());
                    ps.setString(2, cliente.getNombre());
                    ps.setString(3, cliente.getCorreo() == null ? "" : cliente.getCorreo());
                    ps.setString(4, cliente.getTelefono() == null ? "" : cliente.getTelefono());
                    ps.setString(5, cliente.getDireccion() == null ? "" : cliente.getDireccion());
                    ps.setString(6, cliente.getNumeroDPI() == null ? "" : cliente.getNumeroDPI());
                    ps.setString(7, cliente.getNIT() == null ? "" : cliente.getNIT());
                    ps.setString(8, cliente.getEstado() == null ? "ACTIVO" : cliente.getEstado());
                    ps.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("Aviso al persistir cliente en BD: " + e.getMessage());
                } finally {
                    try {
                        cn.close();
                    } catch (SQLException ignored) {
                    }
                }
            }
        } else {
            actualizar(cliente);
        }
    }

    @Override
    public List<Cliente> listar() {
        if (db.getClientes().isEmpty()) {
            db.cargarDatosDesdeBD();
        }
        return new ArrayList<>(db.getClientes());
    }

    @Override
    public Cliente buscarPorId(int id) {
        for (Cliente c : listar()) {
            if (c.getId() == id) {
                return c;
            }
        }
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "SELECT id, nombre, correo, telefono, direccion, numero_dpi, nit, estado FROM cliente WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Cliente c = new Cliente(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("correo"),
                                rs.getString("telefono"),
                                rs.getString("direccion"),
                                rs.getString("numero_dpi"),
                                rs.getString("nit"),
                                rs.getString("estado")
                        );
                        db.getClientes().add(c);
                        return c;
                    }
                }
            } catch (SQLException ignored) {
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public List<Cliente> buscar(String nit, String nombre) {
        List<Cliente> resultado = new ArrayList<>();
        String nLower = nombre == null ? "" : nombre.toLowerCase();
        String nitLower = nit == null ? "" : nit.toLowerCase();

        for (Cliente c : listar()) {
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
        Cliente actual = null;
        for (Cliente c : db.getClientes()) {
            if (c.getId() == cliente.getId()) {
                actual = c;
                break;
            }
        }
        if (actual == null) {
            db.getClientes().add(cliente);
        } else {
            actual.setNombre(cliente.getNombre());
            actual.setCorreo(cliente.getCorreo());
            actual.setTelefono(cliente.getTelefono());
            actual.setDireccion(cliente.getDireccion());
            actual.setNumeroDPI(cliente.getNumeroDPI());
            actual.setNIT(cliente.getNIT());
            actual.setEstado(cliente.getEstado());
        }

        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "UPDATE cliente SET nombre = ?, correo = ?, telefono = ?, direccion = ?, numero_dpi = ?, nit = ?, estado = ? WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, cliente.getNombre());
                ps.setString(2, cliente.getCorreo() == null ? "" : cliente.getCorreo());
                ps.setString(3, cliente.getTelefono() == null ? "" : cliente.getTelefono());
                ps.setString(4, cliente.getDireccion() == null ? "" : cliente.getDireccion());
                ps.setString(5, cliente.getNumeroDPI() == null ? "" : cliente.getNumeroDPI());
                ps.setString(6, cliente.getNIT() == null ? "" : cliente.getNIT());
                ps.setString(7, cliente.getEstado() == null ? "ACTIVO" : cliente.getEstado());
                ps.setInt(8, cliente.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al actualizar cliente en BD: " + e.getMessage());
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
        db.getClientes().removeIf(c -> c.getId() == id);
        Connection cn = ConexionMySQL.getConnection();
        if (cn != null) {
            String sql = "DELETE FROM cliente WHERE id = ?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Aviso al eliminar cliente en BD: " + e.getMessage());
            } finally {
                try {
                    cn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
