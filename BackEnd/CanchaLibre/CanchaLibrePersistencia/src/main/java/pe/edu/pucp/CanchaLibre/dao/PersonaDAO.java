package pe.edu.pucp.CanchaLibre.dao;
import pe.edu.pucp.canchalibre.modelo.Persona;
public interface PersonaDAO<M extends Persona> extends Persistible<M,Integer> {
    M buscarPorNombre(String nombre);
}
