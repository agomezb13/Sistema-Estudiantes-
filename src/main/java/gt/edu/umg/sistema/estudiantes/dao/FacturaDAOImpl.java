package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.conexion.ConexionMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacturaDAOImpl implements FacturaDAO {

    @Override
    public void guardarFactura(String nit, String nombre, String direccion, String fechaEmision, String fechaCertificacion, double subtotal, double iva, double total) {
        String sql = "INSERT INTO factura "
                + "(nit_receptor, nombre_cliente, direccion, fecha_emision, "
                + "fecha_certificacion, subtotal, iva, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionMySQL.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nit);
            ps.setString(2, nombre);
            ps.setString(3, direccion);
            ps.setString(4, fechaEmision);
            ps.setString(5, fechaCertificacion);
            ps.setDouble(6, subtotal);
            ps.setDouble(7, iva);
            ps.setDouble(8, total);

            ps.executeUpdate();
            System.out.println("Factura guardada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al guardar factura");
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarFactura(String nit) {
        String sql = "DELETE FROM factura WHERE nit_receptor = ?";

        try (Connection conexion = ConexionMySQL.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nit);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Factura(s) del NIT " + nit + " eliminada(s) correctamente.");
            } else {
                System.out.println("No se encontró ninguna factura con ese NIT.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar factura");
            e.printStackTrace();
        }
    }
}
