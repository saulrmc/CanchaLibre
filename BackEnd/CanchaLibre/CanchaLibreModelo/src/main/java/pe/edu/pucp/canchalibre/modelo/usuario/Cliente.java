package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.util.List;

public class Cliente extends Persona {
    private double calificacion;

    public double getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
}