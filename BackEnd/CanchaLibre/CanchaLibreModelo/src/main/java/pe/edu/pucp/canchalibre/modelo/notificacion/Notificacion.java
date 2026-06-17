package pe.edu.pucp.canchalibre.modelo.notificacion;
import pe.edu.pucp.canchalibre.modelo.Persona;

import java.time.LocalDateTime;
import java.util.Date;

/*
* Aunque lo normal es que las notificaciones se deriven al correo electrónico,
* el sistema también envia notificaciones (RF07)
* */

public class Notificacion {
    private Persona destinatario;
    private Date fechaEnvio;

    public Date getFechaEnvio() {
        return fechaEnvio;
    }
    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Persona getDestinatario() {
        return destinatario;
    }
    public void setDestinatario(Persona destinatario) {
        this.destinatario = destinatario;
    }
}
