package pe.edu.pucp.CanchaLibre.modelo.Cancha;

public class EsquemaPrecio {
    private int id;
    private double precioHora;
    private boolean conIluminacion; //(RF05) precio varía si la cancha
    //tiene iluminación y se ha utilizado durante el horario reservado
    private Temporada temporada;

    public boolean isConIluminacion() {
        return conIluminacion;
    }

    public void setConIluminacion(boolean conIluminacion) {
        this.conIluminacion = conIluminacion;
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
