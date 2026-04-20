package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Propietario  extends Usuario{
    //TODO: agregar más atributos al propietario (si es que los hay)
    public Propietario(int idUsuario, String nombres,
                         String contrasena, String correo, String telefono) {
        super(idUsuario, nombres, contrasena, correo, telefono);
        rol =  Rol.PROPIETARIO; //así los roles no dependen de que el propio usuario
        //se los coloque
    }
}
