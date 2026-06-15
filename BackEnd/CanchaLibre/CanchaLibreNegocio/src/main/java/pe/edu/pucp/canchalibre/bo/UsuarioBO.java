package pe.edu.pucp.canchalibre.bo;

import pe.edu.pucp.canchalibre.modelo.usuario.Usuario;

public interface UsuarioBO<M extends Usuario> extends Gestionable<M> {
	boolean login(String username,String password);
}