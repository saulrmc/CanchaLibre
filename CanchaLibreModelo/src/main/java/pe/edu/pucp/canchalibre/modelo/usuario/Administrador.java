package pe.edu.pucp.canchalibre.modelo.usuario;
public class Administrador extends Usuario {
    @Override
    public Rol getRol(){return Rol.ADMINISTRADOR;}

}