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
    public PropietarioViewModel? propietario {get; set;} 
    public List<EtiquetaEnum>? etiquetas {get; set;} 
}