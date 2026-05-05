package pe.edu.pucp.CanchaLibre.modelo.usuario;
public class Administrador extends Usuario {
    @Override
    public Rol getRol(){return Rol.ADMINISTRADOR;}

}