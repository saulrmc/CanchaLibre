package pe.edu.pucp.canchalibre.modelo.cancha;

import pe.edu.pucp.canchalibre.modelo.Registro;
import java.time.LocalTime;

public class BloqueHorario extends Registro {
    private DiaSemana dia; //Lunes = 1, ..., Domingo = 7
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double precio;
    private EstadoBloque estado;

    public DiaSemana getDia() {return dia;}
    public void setDia(DiaSemana dia) {this.dia = dia;}

    public LocalTime getHoraInicio() {return horaInicio;}
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public EstadoBloque getEstado() {
        return estado;
    }
    public void setEstado(EstadoBloque estado) {
        this.estado = estado;
    }
}
