package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaDAO;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

public interface PropietarioDAO extends PersonaDAO<Propietario> {
    void actualizarSaldo(Integer idPropietario, double monto);
}
