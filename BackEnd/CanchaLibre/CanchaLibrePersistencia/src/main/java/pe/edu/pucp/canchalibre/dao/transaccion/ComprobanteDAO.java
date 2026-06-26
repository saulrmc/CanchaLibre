package pe.edu.pucp.canchalibre.dao.transaccion;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.transaccion.Comprobante;

public interface ComprobanteDAO extends Persistible<Comprobante,Integer>{
    int insertarComprobante(Comprobante modelo, int idReserva);
}
