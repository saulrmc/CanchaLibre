package pe.edu.pucp.canchalibre.modelo.reserva;
import pe.edu.pucp.canchalibre.modelo.Registro;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.time.LocalDateTime;

public class Reserva extends Registro {
    private EstadoReserva estado;
    private LocalDateTime fechaHoraInicio; //El precio se calcula
    //buscando los bloques de la cancha que caen en este rango
    private LocalDateTime fechaHoraFin;
    private Cliente cliente;
    private Cancha cancha;
    private Pago pago;
    // La duracion podría sacarse de BloqueHorario que posee la Cancha
    // PERO igual se necesita convertir a int
    // No double porque no le vas a mostrar al cliente una duración de 38.99 minutos o algo así
    // private int duracion; La durcion se calcula con Duration.between(fechaHoraInicio, fechaHoraFin).toMinutes()

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

//    public int getDuracion() {
//        return duracion;
//    }
//    public void setDuracion(int duracion) {
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

//    public int getIdReserva() {
//        return idReserva;
//    }
//    public void setIdReserva(int idReserva) {
//        this.idReserva = idReserva;
//    }
}
