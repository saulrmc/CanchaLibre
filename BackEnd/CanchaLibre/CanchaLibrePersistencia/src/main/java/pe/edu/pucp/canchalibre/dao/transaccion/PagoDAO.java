package pe.edu.pucp.canchalibre.dao.transaccion;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.transaccion.Pago;

public interface PagoDAO extends Persistible<Pago,Integer>{
    int insertarPago(Pago modelo, int idReserva);
}
