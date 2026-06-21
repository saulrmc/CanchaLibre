package pe.edu.pucp.canchalibre.modelo.notificacion;

import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;

public class NotificacionBloqueo extends Notificacion{
    // cuando la cuenta se bloquea se le debe enviar un correo
    // electrónico al usuario
    private CuentaUsuario cuentaUsuario;
    public CuentaUsuario getCuentaUsuario() {
        return cuentaUsuario;
    }
    public void setCuentaUsuario(CuentaUsuario cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }
}
