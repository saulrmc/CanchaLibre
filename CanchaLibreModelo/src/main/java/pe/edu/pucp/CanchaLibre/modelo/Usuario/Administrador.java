package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Administrador extends Usuario {
    //TODO: agregar más atributos al administrador (si es que los hay)
    public Administrador(int idUsuario, String nombres,
                         String contrasena, String correo, String telefono) {
        super(idUsuario, nombres, contrasena, correo, telefono);
        rol =  Rol.ADMINISTRADOR; //así los roles no dependen de que el propio usuario
        //se los coloque
    }
}