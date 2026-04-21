package pe.edu.pucp.CanchaLibre.modelo.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;

import java.time.LocalTime;
import java.util.List;

public class BloqueHorario {
    private int id;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean disponible; //la cancha podría estar en mantenimiento o
    //fuera de servicio por un tiempo
    List<Reserva> reservas; //para evitar que se solapen horarios de reservas

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
