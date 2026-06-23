package pe.edu.pucp.canchalibre.dao.cuentas;

import pe.edu.pucp.canchalibre.dao.Persistible;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

import java.util.Map;

public interface CuentaUsuarioDAO extends Persistible<CuentaUsuario, Integer> {
    boolean login(String username, String password);
    void actualizarDatosSeguridad(CuentaUsuario cuenta);
}
