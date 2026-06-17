package pe.edu.pucp.canchalibre.bo;

import pe.edu.pucp.canchalibre.modelo.Persona;

public interface PersonaBO<M extends Persona> extends Gestionable<M> {
	M buscarPorNombre(String nombres);
}