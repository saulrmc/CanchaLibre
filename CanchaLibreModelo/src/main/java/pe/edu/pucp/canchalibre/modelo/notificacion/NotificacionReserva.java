package pe.edu.pucp.canchalibre.modelo.notificacion;

import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

public class NotificacionReserva extends Notificacion {
    private Reserva reserva;
    private String descripcionReserva;

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setDescripcionReserva(String descripcionReserva) {
        this.descripcionReserva = descripcionReserva;
    }

    public String getDescripcionReserva() {
        return descripcionReserva;
    }
}

