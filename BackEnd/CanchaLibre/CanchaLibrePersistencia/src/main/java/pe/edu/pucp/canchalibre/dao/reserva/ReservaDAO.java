package pe.edu.pucp.canchalibre.dao.reserva;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.util.List;

public interface ReservaDAO extends Persistible<Reserva,Integer> {
    List<Reserva> listarReservasPorCuenta(String cuenta);
    List<Reserva> listarPorClienteId(int idCliente);
}
