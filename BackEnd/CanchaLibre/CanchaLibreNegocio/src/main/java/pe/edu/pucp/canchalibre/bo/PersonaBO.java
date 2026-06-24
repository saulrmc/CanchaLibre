package pe.edu.pucp.canchalibre.bo;

import pe.edu.pucp.canchalibre.modelo.Persona;

import java.util.List;

public interface PersonaBO<M extends Persona> extends Gestionable<M> {
	List<M> buscarPorNombre(String nombres);
	M buscarPorCuenta(String cuenta); //userName
}