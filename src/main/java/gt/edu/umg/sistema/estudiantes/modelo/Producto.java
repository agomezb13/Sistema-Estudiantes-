package gt.edu.umg.sistema.estudiantes.modelo;

public class Producto {

    private int id;
    private int categoriaId;
    private String nombre;
    private String descripcion;
    private double precio;
    private int existencias;

    public Producto() {
    }

    public Producto(int id, int categoriaId, String nombre, String descripcion, double precio, int existencias) {
        this.id = id;
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.existencias = existencias;
    }

    public void agregarAlCarrito() {
    }

    public void actualizarStock(int cantidad) {
        this.existencias += cantidad;
    }

    public void actualizarStock() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getExistencias() {
        return existencias;
    }

    public void setExistencias(int existencias) {
        this.existencias = existencias;
    }

    @Override
    public String toString() {
        return nombre + " (Q. " + String.format("%.2f", precio) + ")";
    }
}
