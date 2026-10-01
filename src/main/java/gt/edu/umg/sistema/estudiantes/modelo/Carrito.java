package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Carrito {

    private int id;
    private int clienteId;
    private Date fechaCreacion;
    private String estado;
    private List<ElementoCarrito> elementos;

    public Carrito() {
        this.fechaCreacion = new Date();
        this.estado = "ACTIVO";
        this.elementos = new ArrayList<>();
    }

    public Carrito(int id, int clienteId, Date fechaCreacion, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.elementos = new ArrayList<>();
    }

    public void agregar(ElementoCarrito elemento) {
        this.elementos.add(elemento);
    }

    public void agregar() {
    }

    public void quitar(int elementoId) {
        this.elementos.removeIf(e -> e.getId() == elementoId);
    }

    public void quitar() {
        if (!elementos.isEmpty()) {
            elementos.remove(elementos.size() - 1);
        }
    }

    public double calcularTotal() {
        double total = 0.0;
        for (ElementoCarrito e : elementos) {
            total += e.calcularSubtotal();
        }
        return total;
    }

    public void vaciar() {
        this.elementos.clear();
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<ElementoCarrito> getElementos() {
        return elementos;
    }

    public void setElementos(List<ElementoCarrito> elementos) {
        this.elementos = elementos;
    }
}
