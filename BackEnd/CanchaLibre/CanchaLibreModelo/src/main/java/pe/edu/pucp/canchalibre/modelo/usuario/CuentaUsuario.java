package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Registro;
import java.time.LocalDateTime;

public class CuentaUsuario extends Registro {
    private String userName;
    private String password;
    private Rol rol;
    private int intentosFallidos; //bloquear inicio de sesión después de 3 intentos
    private LocalDateTime ultimaSesion; //medir que el límite de fallos no sobrepase el minuto
    private LocalDateTime fechaBloqueo;

    public Rol getRol(){return rol;}
    public void setRol(Rol rol){this.rol=rol;}

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }
    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public LocalDateTime getUltimaSesion() {
        return ultimaSesion;
    }
    public void setUltimaSesion(LocalDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public LocalDateTime getFechaBloqueo() {
        return fechaBloqueo;
    }
    public void setFechaBloqueo(LocalDateTime fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    @Override
    public String toString() {
        return "CuentaUsuario{" +
                "id=" + getId() +
                ", activo=" + isActivo() +
                ", userName='" + userName + '\'' +
                ", password='" + (password != null ?
                "*".repeat(password.length()) : null) + '\'' +
                '}';
    }
}
