package pe.edu.pucp.CanchaLibre.modelo.Cancha;

public class EsquemaPrecio {
    private int id;
    private double precioHora;
    private boolean conIluminacion; //(RF05) precio varía si la cancha
    //tiene iluminación y se ha utilizado durante el horario rservado
    private BloqueHorario bloqueHorario;
    private Cancha cancha;

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public BloqueHorario getBloqueHorario() {
        return bloqueHorario;
    }

    public void setBloqueHorario(BloqueHorario bloqueHorario) {
        this.bloqueHorario = bloqueHorario;
    }

    public boolean isConIlumincacion() {
        return conIluminacion;
    }

    public void setConIlumincacion(boolean conIlumincacion) {
        this.conIluminacion = conIlumincacion;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(double precioHora) {
        this.precioHora = precioHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
