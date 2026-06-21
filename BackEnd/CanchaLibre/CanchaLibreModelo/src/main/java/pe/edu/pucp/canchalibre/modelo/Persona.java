package pe.edu.pucp.canchalibre.modelo;

import pe.edu.pucp.canchalibre.modelo.usuario.CuentaUsuario;
import pe.edu.pucp.canchalibre.modelo.usuario.Rol;

public abstract class Persona extends Registro {
    private String nombres;
    private String correo;
    private String telefono; //contacto
    private CuentaUsuario cuentaUsuario;

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public CuentaUsuario getCuentaUsuario() {
        return cuentaUsuario;
    }
    public void setCuentaUsuario(CuentaUsuario cuentaUsuario) {
        this.cuentaUsuario = cuentaUsuario;
    }

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}
