/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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

}
