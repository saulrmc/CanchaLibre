package pe.edu.pucp.CanchaLibre.modelo.usuario;

import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;

import java.util.List;

public class Propietario  extends Usuario{
    private List<Cancha> canchas;
    private int calificacion;

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<Cancha> canchas) {
        this.canchas = canchas;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    @Override
    public Rol getRol(){return Rol.PROPIETARIO;}
}
