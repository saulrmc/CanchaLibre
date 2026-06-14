package pe.edu.pucp.CanchaLibre.dao;
import pe.edu.pucp.canchalibre.modelo.usuario.Usuario;
public interface UsuarioDAO<M extends Usuario> extends Persistible<M,Integer> {
    M buscarPorNombre(String nombres);
}
