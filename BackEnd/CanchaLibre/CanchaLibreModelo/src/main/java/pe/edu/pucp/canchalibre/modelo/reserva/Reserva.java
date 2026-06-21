package pe.edu.pucp.canchalibre.modelo.reserva;
import pe.edu.pucp.canchalibre.modelo.Registro;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.time.LocalDateTime;

public class Reserva extends Registro {
    private EstadoReserva estado;
    private LocalDateTime fechaHora;
    private Cliente cliente;
    private Cancha cancha;
    private Pago pago;
    // La duracion podría sacarse de BloqueHorario que posee la Cancha
//    private LocalDateTime duracion;
//
//    public LocalDateTime getDuracion() {
//        return duracion;
//    }
//    public void setDuracion(LocalDateTime duracion) {
//        this.duracion = duracion;
//    }

    public Pago getPago() {
        return pago;
    }
    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Cancha getCancha() {
        return cancha;
    }
    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

//    public int getIdReserva() {
//        return idReserva;
//    }
//    public void setIdReserva(int idReserva) {
//        this.idReserva = idReserva;
//    }
}
