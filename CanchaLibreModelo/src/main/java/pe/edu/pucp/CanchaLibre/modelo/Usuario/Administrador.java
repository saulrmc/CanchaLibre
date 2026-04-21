package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nombres, String contrasena, String correo, String telefono) {
        super(idUsuario, nombres, contrasena, correo, telefono);
    }

    @Override
    public Rol getRol(){return Rol.ADMINISTRADOR;}

}