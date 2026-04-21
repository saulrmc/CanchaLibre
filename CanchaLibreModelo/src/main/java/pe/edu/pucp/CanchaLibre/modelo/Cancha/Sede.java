package pe.edu.pucp.CanchaLibre.modelo.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Propietario;
import java.util.List;
import java.util.ArrayList;

public class Sede {
    private int idSede;
    private String nombre;
    private String direccion;
    private Propietario propietario;
    private List<Cancha> canchas; //el motivo es que muchas veces
    //hay más de una cancha en una misma ubicación, ej.
    //la cancha del estadio Lolo Fernández. Aunque físicamente es una
    //sola cancha en la realidad casi nunca se utiliza en su totalidad sino
    //que se dividen sectores, por lo que se pueden obtener 4 canchas distintas

}
