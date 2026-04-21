package pe.edu.pucp.CanchaLibre.modelo.Usuario;
public class Administrador extends Usuario {
    @Override
    public Rol getRol(){return Rol.ADMINISTRADOR;}

}