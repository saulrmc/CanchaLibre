package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Cliente extends Usuario {
    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    private int calificacion;
    @Override
    public Rol getRol(){return Rol.CLIENTE;}

}