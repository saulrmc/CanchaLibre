package pe.edu.pucp.CanchaLibre.modelo.transaccion;

import pe.edu.pucp.CanchaLibre.modelo.reserva.Reserva;

import java.time.LocalDateTime;

public class Pago {
    private int id;
    private MetodoPago metodoPago;
    private double monto;
    private LocalDateTime fechaPago;

    private Reserva reserva; // NUEVO

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
}
