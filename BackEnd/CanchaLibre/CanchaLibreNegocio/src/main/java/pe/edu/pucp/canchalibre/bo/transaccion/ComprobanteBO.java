package pe.edu.pucp.canchalibre.bo.transaccion;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

public interface ComprobanteBO extends Gestionable<Comprobante> {
    int insertarComprobante(Comprobante modelo, int idReserva);
}