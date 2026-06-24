package pe.edu.pucp.canchalibre.bo.cancha;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.util.List;

public interface CanchaBO extends Gestionable<Cancha> {
    List<Cancha> listarCanchasPorCuenta(String cuenta); //canchas por propietario, conectado al userName
}