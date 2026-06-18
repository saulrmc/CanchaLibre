package pe.edu.pucp.canchalibre.modelo.cancha;
import pe.edu.pucp.canchalibre.modelo.Registro;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.util.List;

public class Cancha extends Registro {
    //DETALLE
    //(id, activo) : Registro
    private String nombre;
    private String descripcion;
    private String direccion;
    private String imagenUrl;
    private Propietario propietario;
    private List<Deporte> deportes;
    private List<Etiqueta> etiquetas;

    //REVIEWS
//    private List<Resena> resenas;

    //HORARIO
    private List<BloqueHorario> bloques;
    private int horaApertura;
    private int horaCierre;
    private double precioBase;

    public int getHoraApertura() { return horaApertura;}
    public void setHoraApertura(int horaApertura) { this.horaApertura = horaApertura;}

    public int getHoraCierre() { return horaCierre;}
    public void setHoraCierre(int horaCierre) { this.horaCierre = horaCierre;}

    public List<BloqueHorario> getBloques() {return bloques;}
    public void setBloques(List<BloqueHorario> bloques) {this.bloques = bloques;}

    public double getPrecioBase() {return precioBase;}
    public void setPrecioBase(double precioBase) {this.precioBase = precioBase;}

//    public List<Resena> getResenas() {
//        return resenas;
//    }
//    public void setResenas(List<Resena> resenas) {
//        this.resenas = resenas;
//    }

    public List<Etiqueta> getEtiquetas() {return etiquetas; }
    public void setEtiquetas(List<Etiqueta> etiquetas) { this.etiquetas = etiquetas; }

    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public String getImagenUrl() {return imagenUrl;}
    public void setImagenUrl(String imagenUrl) {this.imagenUrl = imagenUrl;}

    public List<Deporte> getDeportes() {return deportes;}
    public void setDeportes(List<Deporte> deportes) {this.deportes = deportes;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public String getDireccion() { return direccion;}
    public void setDireccion(String direccion) { this.direccion = direccion;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

}

