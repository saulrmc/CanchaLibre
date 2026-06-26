package pe.edu.pucp.canchalibre.bo.cuentas;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

public interface CuentaUsuarioBO extends Gestionable<CuentaUsuario> {
    boolean login(String username, String password);
    void actualizarDatosSeguridad(CuentaUsuario cuenta);
    //Persona buscarPersonaPorUsername(String username);
}
