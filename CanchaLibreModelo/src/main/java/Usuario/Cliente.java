package Usuario;
public class Cliente extends Usuario {
    //TODO: agregar más atributos al cliente (si es que los hay)
    private String datosPago;
    public Cliente(int idUsuario, String nombres,
                         String contrasena, String correo, String telefono) {
        super(idUsuario, nombres, contrasena, correo, telefono);
        rol =  Rol.CLIENTE; //así los roles no dependen de que el propio usuario
        //se los coloque
    }
}