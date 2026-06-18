package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.util.List;

public class Cliente extends Persona {
//    private List<Reserva> reservas; //RF09
//    private List<Resena> resenas;
    private int calificacion;
//
//    public List<Reserva> getReservas() {
//        return reservas;
//    }
//    public void setReservas(List<Reserva> reservas) {
//        this.reservas = reservas;
//    }
//
//    public List<Resena> getResenas() {
//        return resenas;
//    }
//    public void setResenas(List<Resena> resenas) {
//        this.resenas = resenas;
//    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    @Override
    public Rol getRol(){return Rol.CLIENTE;}

}