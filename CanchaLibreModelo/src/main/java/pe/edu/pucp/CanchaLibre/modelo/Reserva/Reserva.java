package pe.edu.pucp.CanchaLibre.modelo.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Pago;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Reserva {
    private int idReserva;
    private LocalDateTime fechaHora;
    private EstadoReserva estado;
    private Cliente cliente;
    private Cancha cancha;
    private Pago pago;
    private LocalTime duracion;

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

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

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
}
