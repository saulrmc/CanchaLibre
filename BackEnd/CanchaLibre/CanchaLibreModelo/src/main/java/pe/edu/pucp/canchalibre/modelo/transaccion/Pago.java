package pe.edu.pucp.canchalibre.modelo.transaccion;


import pe.edu.pucp.canchalibre.modelo.Registro;

import java.time.LocalDateTime;

public class Pago extends Registro {
    private MetodoPago metodoPago;
    private double monto;
    private LocalDateTime fechaPago;
    //private Comprobante comprobante;

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

//    public Comprobante getComprobante() {
//        return comprobante;
//    }
//    public void setComprobante(Comprobante comprobante) {
//        this.comprobante = comprobante;
//    }

}
