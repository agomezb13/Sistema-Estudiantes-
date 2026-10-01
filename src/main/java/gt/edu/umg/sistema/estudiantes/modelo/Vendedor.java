package gt.edu.umg.sistema.estudiantes.modelo;

public class Vendedor {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private String estado;

    public Vendedor() {
        this.estado = "ACTIVO";
    }

    public Vendedor(int id, String nombre, String correo, String telefono, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }

    public Factura crearFactura() {
        return new Factura();
    }

    public void consultarVentas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
