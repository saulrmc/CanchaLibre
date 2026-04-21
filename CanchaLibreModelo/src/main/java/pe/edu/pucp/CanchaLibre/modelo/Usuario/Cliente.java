package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Cliente extends Usuario {
    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    private int calificacion;

    public Cliente(int idUsuario, String nombres, String contrasena, String correo, String telefono) {
        super(idUsuario, nombres, contrasena, correo, telefono);
    }

    @Override
    public Rol getRol(){return Rol.CLIENTE;}

}