using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
public sealed class CanchaRestDto
{
    public int idCancha {get; set;} 
    public String nombre {get; set;} = string.Empty;
    public String descripcion {get; set;} = string.Empty;
    public List<String>? deportes {get; set;} 
    public String imagenUrl {get; set;} = string.Empty;
    public bool disponible {get; set;} 
    public String direccion {get; set;} = string.Empty;
    public PropietarioRestDto? propietario {get; set;} 
    public List<String>? etiquetas {get; set;} 
    public List<BloqueHorarioRestDto>? bloques {get; set;}
    public double precioBase {get; set;}
    public double promedioCalificacion{get; set;}
    public bool activo {get; set;}
}