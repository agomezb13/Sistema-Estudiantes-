package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {

    private int id;
    private int clienteId;
    private int direccionEnvioId;
    private Date fecha;
    private String estado;
    private double total;
    private List<DetallePedido> detalles;

    public Pedido() {
        this.fecha = new Date();
        this.estado = "PENDIENTE";
        this.detalles = new ArrayList<>();
    }

    public Pedido(int id, int clienteId, int direccionEnvioId, Date fecha, String estado, double total) {
        this.id = id;
        this.clienteId = clienteId;
        this.direccionEnvioId = direccionEnvioId;
        this.fecha = fecha;
        this.estado = estado;
        this.total = total;
        this.detalles = new ArrayList<>();
    }

    public void confirmar() {
        this.estado = "CONFIRMADO";
    }

    public void cancelar() {
        this.estado = "CANCELADO";
    }

    public double calcularTotal() {
        double acum = 0.0;
        for (DetallePedido d : detalles) {
            acum += d.getSubtotal();
        }
        this.total = acum;
        return this.total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getDireccionEnvioId() {
        return direccionEnvioId;
    }

    public void setDireccionEnvioId(int direccionEnvioId) {
        this.direccionEnvioId = direccionEnvioId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " (" + estado + " - Q. " + String.format("%.2f", total) + ")";
    }
}
