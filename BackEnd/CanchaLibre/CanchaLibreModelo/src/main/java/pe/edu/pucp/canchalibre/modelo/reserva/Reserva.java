package pe.edu.pucp.canchalibre.modelo.reserva;

import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.time.LocalDateTime;
import java.util.List;

public class Reserva {
    private int idReserva;
    private EstadoReserva estado;
    private Cliente cliente;
    private Cancha cancha;
    private LocalDateTime fechaCreacion;
    private Pago pago;
    private List<BloqueHorario> bloquesSeleccionados;

    public List<BloqueHorario> getBloquesSeleccionados() {return bloquesSeleccionados;}
    public void setBloquesSeleccionados(List<BloqueHorario> bloquesSeleccionados) {this.bloquesSeleccionados = bloquesSeleccionados;}

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public int getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }
}
