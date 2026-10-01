package gt.edu.umg.sistema.estudiantes.modelo;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private int id;
    private List<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public Inventario(int id) {
        this.id = id;
        this.productos = new ArrayList<>();
    }

    public List<Producto> buscar() {
        return productos;
    }

    public List<Producto> filtrar(int categoriaId) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCategoriaId() == categoriaId) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    public List<Producto> filtrar() {
        return productos;
    }

    public Producto verDetalle(int productoId) {
        for (Producto p : productos) {
            if (p.getId() == productoId) {
                return p;
            }
        }
        return null;
    }

    public Producto verDetalle() {
        return productos.isEmpty() ? null : productos.get(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
