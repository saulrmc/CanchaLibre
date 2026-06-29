package pe.edu.pucp.canchalibre.dao.cancha;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.cancha.BloqueHorario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BloqueHorarioDAO extends Persistible<BloqueHorario, Integer> {
    void crearBloquesPorCancha(Connection conn,
                                Integer idCancha,
                                List<BloqueHorario> bloques) throws SQLException;
    List<BloqueHorario> leerBloquesPorCancha(Connection conn,
                                              Integer idCancha) throws SQLException;
    void eliminarBloquePorCancha(Connection conn,
                                  Integer idCancha) throws SQLException;


    void crearBloquesPorReserva(Connection conn,
                                Integer idReserva,
                                List<BloqueHorario> bloques) throws SQLException;
    List<BloqueHorario> leerBloquesPorReserva(Connection conn,
                                              Integer idReserva) throws SQLException;

    Map<Integer, List<BloqueHorario>> leerBloquesTodasCanchas(Connection conn) throws SQLException;

    Map<Integer, List<BloqueHorario>> leerBloquesTodasReservas(Connection conn) throws SQLException;
}
