package pe.edu.pucp.CanchaLibre.modelo.Usuario;

import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;

import java.util.List;

public class Propietario  extends Usuario{
    private List<Cancha> canchas;

    public List<Cancha> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<Cancha> canchas) {
        this.canchas = canchas;
    }

    @Override
    public Rol getRol(){return Rol.PROPIETARIO;}
}
