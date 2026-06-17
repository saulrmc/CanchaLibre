package pe.edu.pucp.CanchaLibre.dao.cuentas;

import pe.edu.pucp.CanchaLibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

public interface CuentaUsuarioDAO extends Persistible<CuentaUsuario, Integer> {
    boolean login(String username, String password);
}
