package pe.edu.pucp.CanchaLibre.modelo.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Usuario;

import java.time.LocalDateTime;

/*
* Aunque lo normal es que las notificaciones se deriven al correo electrónico,
* el sistema también envia notificaciones (RF07)
* */
public class Notificacion {
    private Usuario destinatario;
    private String descripcion;
    private LocalDateTime fechaEnvio;

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
}
