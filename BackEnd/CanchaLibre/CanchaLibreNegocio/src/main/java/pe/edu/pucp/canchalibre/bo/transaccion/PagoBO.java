package pe.edu.pucp.canchalibre.bo.transaccion;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;

public interface PagoBO extends Gestionable<Pago> {
    int insertarPago(Pago modelo, int idReserva);
}