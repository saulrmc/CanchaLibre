package pe.edu.pucp.canchalibre.dao;
import pe.edu.pucp.canchalibre.modelo.Persona;
public interface PersonaDAO<M extends Persona> extends Persistible<M,Integer> {
    M buscarPorNombre(String nombre);
}
