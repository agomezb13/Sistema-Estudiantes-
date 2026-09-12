/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gt.edu.umg.sistema.estudiantes.modelo;
 
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


/**
 *
 * @author Angel Gomez
 */
public class Factura {
    
    private int idFactura;
    private LocalDate fecha;
    private Cliente cliente;
    private List<DetalleFactura> detalles;
    
    public Factura(){
        detalles=new ArrayList<>();
    }
    public void agegarDetalle(DetalleFactura detalle){
        detalles.add(detalle);
    }
    public double calcularTotal(){
        double total=0;
        for(DetalleFactura detalle : detalles){
            total+=detalle.calcularSubtotal();
        }
        return total;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
}
