package pe.edu.pucp.CanchaLibre.modelo.Cancha;
import java.util.List;

public class Cancha {
    private int idCancha;
    private String nombre;
    private String descripcion;
    private String direccion;
    private double precioPorHora;
    private List<TipoCancha> tipos;

    public Cancha() {
    }

    public List<TipoCancha> getTipos() {
        return tipos;
    }

    public void setTipos(List<TipoCancha> tipos) {
        this.tipos = tipos;
    }

    public double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCancha() {
        return idCancha;
    }

    public void setIdCancha(int idCancha) {
        this.idCancha = idCancha;
    }
}

