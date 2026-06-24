package pe.edu.pucp.canchalibre.bo.resena;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.resena.Resena;

import java.util.List;

public interface ResenaBO extends Gestionable<Resena> {
    List<Resena> listarResenasPorCancha(Integer idCancha);
    List<Resena> listarResenasPorCliente(Integer idCliente);
}