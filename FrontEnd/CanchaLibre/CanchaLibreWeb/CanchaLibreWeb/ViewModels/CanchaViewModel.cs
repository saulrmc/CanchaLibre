using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class CanchaViewModel
{
    public int idCancha {get; set;} 

    [Required(ErrorMessage = "Debe agregar un nombre")]
    public String nombre {get; set;} = string.Empty;

    [Required(ErrorMessage = "Debe agregar una descripción")]
    public String descripcion {get; set;} = string.Empty;
    public List<DeporteEnum>? deportes {get; set;} 

    [Required(ErrorMessage = "Debe agregar una imagen")]
    public String imagenUrl {get; set;} = string.Empty;
    public bool disponible {get; set;} 

    [Required(ErrorMessage = "Debe agregar una dirección")]
    public String direccion {get; set;} = string.Empty;

    [Required(ErrorMessage = "Debe seleccionar un distrito")]

    public string distrito { get; set; } = string.Empty;

    public PropietarioViewModel? propietario {get; set;} 
    public List<EtiquetaEnum>? etiquetas {get; set;} 

    [Required(ErrorMessage = "Debe agregar al menos un bloque de horario")]
    public List<BloqueHorarioViewModel>? bloques {get; set;}

    [Required(ErrorMessage = "Debe agregar un precio base")]
    [Range(1, 500, ErrorMessage = "El precio debe ser mayor a 0.")]
    public double precioBase {get; set;}
    public double promedioCalificacion{get; set;}
    public bool activo {get; set;}

}