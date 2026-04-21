package pe.edu.pucp.CanchaLibre.modelo.Usuario;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;
import java.util.List;
import java.util.ArrayList;

public class Propietario  extends Usuario{
    private int calificacion;
    private List<Cancha> canchas;

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<Cancha> canchas) {
        this.canchas = canchas;
    }

    @Override
    public Rol getRol(){return Rol.PROPIETARIO;}
}
