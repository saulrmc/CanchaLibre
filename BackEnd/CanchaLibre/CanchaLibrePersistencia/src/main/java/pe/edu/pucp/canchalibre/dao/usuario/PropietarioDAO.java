package pe.edu.pucp.canchalibre.dao.usuario;

import pe.edu.pucp.canchalibre.dao.PersonaDAO;
import pe.edu.pucp.canchalibre.modelo.usuario.Propietario;

import java.sql.Connection;
import java.sql.SQLException;

public interface PropietarioDAO extends PersonaDAO<Propietario> {
    double actualizarSaldo(Connection conn,
                         Integer idPropietario,
                         double monto) throws SQLException;
}
