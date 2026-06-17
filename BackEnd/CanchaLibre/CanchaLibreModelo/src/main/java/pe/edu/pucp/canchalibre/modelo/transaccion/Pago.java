package pe.edu.pucp.canchalibre.modelo.transaccion;

import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.util.Date;

public class Pago {
    private int idPago;
    private MetodoPago metodoPago;
    private double monto;
    private Date fechaPago;
    private Comprobante comprobante;

    public int getIdPago() {
        return idPago;
    }
    public void setIdPago(int idPago) {
        this.idPago = idPago;
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

    public Date getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Comprobante getComprobante() {
        return comprobante;
    }
    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

}
