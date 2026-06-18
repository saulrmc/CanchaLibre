package pe.edu.pucp.CanchaLibre.dao.cancha;

import pe.edu.pucp.CanchaLibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;

import java.util.List;

public interface CanchaDAO extends Persistible<Cancha,Integer> {
    List<Cancha> listarCanchasPorCuenta(String cuenta); //canchas por propietario, conectado al userName
}
