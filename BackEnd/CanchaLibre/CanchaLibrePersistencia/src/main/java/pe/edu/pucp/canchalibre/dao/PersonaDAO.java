package pe.edu.pucp.canchalibre.dao;
import pe.edu.pucp.canchalibre.modelo.Persona;

import java.util.List;

public interface PersonaDAO<M extends Persona> extends Persistible<M,Integer> {
    M buscarPorNombre(String nombres);
    M buscarPorCuenta(String cuenta); //userName
}
