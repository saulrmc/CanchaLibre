package pe.edu.pucp.canchalibre.bo.reserva;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;

import java.util.List;

public interface ReservaBO extends Gestionable<Reserva> {
    List<Reserva> listarPorClienteId(int idCliente);
    List<Reserva> listarPorClienteCuenta(String Cuenta);
}