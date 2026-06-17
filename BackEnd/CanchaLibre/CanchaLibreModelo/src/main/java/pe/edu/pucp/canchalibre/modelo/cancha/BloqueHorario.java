package pe.edu.pucp.canchalibre.modelo.cancha;

import java.time.LocalDateTime;

public class BloqueHorario {
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private double precio;
    private EstadoBloque estado;

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(LocalDateTime horaFin) {
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
