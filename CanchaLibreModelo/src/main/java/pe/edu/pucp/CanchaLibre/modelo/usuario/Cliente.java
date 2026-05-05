package pe.edu.pucp.CanchaLibre.modelo.usuario;

import pe.edu.pucp.CanchaLibre.modelo.reserva.Reserva;

import java.util.List;

public class Cliente extends Usuario {
    private List<Reserva> historialReservas; //RF09
    private int calificacion;

    public List<Reserva> getHistorialReservas() {
        return historialReservas;
    }

    public void setHistorialReservas(List<Reserva> historialReservas) {
        this.historialReservas = historialReservas;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
    @Override
    public Rol getRol(){return Rol.CLIENTE;}

}