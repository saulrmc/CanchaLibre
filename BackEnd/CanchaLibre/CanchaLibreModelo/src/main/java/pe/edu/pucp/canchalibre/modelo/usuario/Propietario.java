package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.util.List;

public class Propietario extends Persona {
    private double calificacion;
    private String telefonoOperaciones;

    public double getCalificacion() {return calificacion;}
    public void setCalificacion(double calificacion) {this.calificacion = calificacion;}

    public String getTelefonoOperaciones() {return telefonoOperaciones;}
    public void setTelefonoOperaciones(String telefonoOperaciones) {this.telefonoOperaciones = telefonoOperaciones;}
}
