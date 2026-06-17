package pe.edu.pucp.canchalibre.modelo.usuario;

import pe.edu.pucp.canchalibre.modelo.Persona;

public class Administrador extends Persona {
    @Override
    public Rol getRol(){return Rol.ADMINISTRADOR;}
}