package gt.edu.umg.sistema.estudiantes.dao;

public interface FacturaDAO {

    void guardarFactura(
            String nit,
            String nombre,
            String direccion,
            String fechaEmision,
            String fechaCertificacion,
            double subtotal,
            double iva,
            double total
    );

    void eliminarFactura(String nit);
}
