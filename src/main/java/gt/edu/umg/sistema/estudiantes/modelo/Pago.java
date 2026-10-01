package gt.edu.umg.sistema.estudiantes.modelo;

public class Pago {

    private int id;
    private int pedidoId;
    private double monto;
    private String metodo;
    private String estado;

    public Pago() {
        this.estado = "PENDIENTE";
    }

    public Pago(int id, int pedidoId, double monto, String metodo, String estado) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.monto = monto;
        this.metodo = metodo;
        this.estado = estado;
    }

    public boolean procesar() {
        this.estado = "PAGADO";
        return true;
    }

    public boolean reembolsar() {
        this.estado = "REEMBOLSADO";
        return true;
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pago #" + id + " - Q. " + String.format("%.2f", monto) + " (" + metodo + ")";
    }
}
