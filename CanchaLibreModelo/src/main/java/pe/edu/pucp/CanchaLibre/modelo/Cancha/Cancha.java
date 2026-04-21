package pe.edu.pucp.CanchaLibre.modelo.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;

import java.util.List;
import java.util.ArrayList;

public class Cancha {
    private int idCancha;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private boolean disponible;
    private String direccion;
    private List<Deporte> deportes;
    private List<Reserva> reservas;
    private List<EsquemaPrecio> esquemasPrecios;

    public List<EsquemaPrecio> getEsquemasPrecios() {
        return esquemasPrecios;
    }

    public void setEsquemasPrecios(List<EsquemaPrecio> esquemasPrecios) {
        this.esquemasPrecios = esquemasPrecios;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isDisponible() {
        return disponible;
    }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public List<Deporte> getDeportes() {
        return deportes;
    }

    public void setDeportes(List<Deporte> deportes) {
        this.deportes = deportes;
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

