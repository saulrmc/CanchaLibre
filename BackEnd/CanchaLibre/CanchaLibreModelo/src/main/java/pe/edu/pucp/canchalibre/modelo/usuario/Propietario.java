package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.util.List;

public class Propietario  extends Persona {
//    private List<Cancha> canchas;
    private int calificacion;

//    public List<Cancha> getCanchas() {
//        return canchas;
//    }
//    public void setCanchas(List<Cancha> canchas) {
//        this.canchas = canchas;
//    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

}
