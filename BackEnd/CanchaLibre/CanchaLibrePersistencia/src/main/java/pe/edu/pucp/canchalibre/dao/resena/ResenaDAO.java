package pe.edu.pucp.canchalibre.dao.resena;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ResenaDAO extends Persistible<Resena,Integer> {
    List<Resena> listarResenasPorCancha(Integer idCancha);
    List<Resena> listarResenasPorCliente(Integer idCliente);
}
