package pe.edu.pucp.canchalibre.modelo.notificacion;
import pe.edu.pucp.canchalibre.modelo.usuario.Usuario;

import java.time.LocalDateTime;

/*
* Aunque lo normal es que las notificaciones se deriven al correo electrónico,
* el sistema también envia notificaciones (RF07)
* */

public class Notificacion {
    private Usuario destinatario;
    private LocalDateTime fechaEnvio;

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
}
