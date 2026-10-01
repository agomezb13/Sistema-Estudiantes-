package gt.edu.umg.sistema.estudiantes.modelo;

public class DireccionEnvio {

    private int id;
    private int clienteId;
    private String calle;
    private String ciudad;
    private String codigoPostal;
    private String pais;

    public DireccionEnvio() {
    }

    public DireccionEnvio(int id, int clienteId, String calle, String ciudad, String codigoPostal, String pais) {
        this.id = id;
        this.clienteId = clienteId;
        this.calle = calle;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.pais = pais;
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

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return calle + ", " + ciudad + " (" + pais + ")";
    }
}
