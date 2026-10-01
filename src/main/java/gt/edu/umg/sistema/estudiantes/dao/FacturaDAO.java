package gt.edu.umg.sistema.estudiantes.dao;

import gt.edu.umg.sistema.estudiantes.modelo.Factura;
import java.util.List;

public interface FacturaDAO {
    void guardar(Factura factura);
    List<Factura> listar();
    Factura buscarPorId(int id);
    List<Factura> buscar(String numero, Integer clienteId);
    void anular(int id);
    void eliminar(int id);

    // Compatibilidad
    void guardarFactura(String nit, String nombre, String direccion, String fechaEmision, String fechaCertificacion, double subtotal, double iva, double total);
    void eliminarFactura(String nit);
}
