package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factura {

    private int id;
    private int pedidoId;
    private int clienteId;
    private int vendedorId;
    private String numero;
    private Date fechaEmision;
    private double subtotal;
    private double impuesto;
    private double total;
    private String estado;
    private List<DetalleFactura> detalles;

    public Factura() {
        this.fechaEmision = new Date();
        this.estado = "EMITIDA";
        this.detalles = new ArrayList<>();
    }

    public Factura(int id, int pedidoId, int clienteId, int vendedorId, String numero, Date fechaEmision, double subtotal, double impuesto, double total, String estado) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
        this.estado = estado;
        this.detalles = new ArrayList<>();
    }

    public void generar() {
        this.estado = "EMITIDA";
        calcularTotal();
    }

    public double calcularTotal() {
        double acum = 0.0;
        for (DetalleFactura d : detalles) {
            acum += d.calcularSubtotal();
        }
        this.subtotal = acum;
        this.impuesto = this.subtotal * 0.12;
        this.total = this.subtotal + this.impuesto;
        return this.total;
    }

    public void anular() {
        this.estado = "ANULADA";
    }

    public void agegarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }

    public void agregarDetalle(DetalleFactura detalle) {
        this.detalles.add(detalle);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Factura " + numero + " - Q. " + String.format("%.2f", total);
    }
}
