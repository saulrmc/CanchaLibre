package pe.edu.pucp.canchalibre.modelo.notificacion;

import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

public class NotificacionComprobante extends Notificacion {
    private Comprobante comprobante;
    private String descripcionComprobante;

    public Comprobante getComprobante() {
        return comprobante;
    }
    public void setComprobante(Comprobante comprobante) {
        this.comprobante = comprobante;
    }

    public void setDescripcionComprobante(String descripcionComprobante) {this.descripcionComprobante = descripcionComprobante;}
    public String getDescripcionComprobante() {
        return descripcionComprobante;
    }
}
