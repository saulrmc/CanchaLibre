package pe.edu.pucp.canchalibre.bo.cuentas;

import pe.edu.pucp.canchalibre.bo.Gestionable;
import pe.edu.pucp.canchalibre.modelo.Persona;
import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

public interface CuentaUsuarioBO extends Gestionable<CuentaUsuario> {
    Persona login(String username, String password);
    CuentaUsuario buscarCuenta(String username);
    void actualizarDatosSeguridad(CuentaUsuario cuenta);
}
