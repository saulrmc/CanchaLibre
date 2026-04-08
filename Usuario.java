public abstract class Usuario {
    private int idUsuario;
    private String nombres;
    private String contrasena;
    private String correo;
    private String telefono;
    protected Rol rol;

    Usuario(int idUsuario, String nombres, String contrasena,
            String correo, String telefono) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.contrasena = contrasena;
        this.correo = correo;
        this.telefono = telefono;
    }

    public boolean tienePermiso(Permiso permiso) {
        return rol.getTiposPermiso().contains(permiso);
    }

    public Rol getRol() {
        return rol;
    }

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
