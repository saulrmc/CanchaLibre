using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class EsquemaPrecioViewModel 
{
    public int id {get;set;}
    [Required(ErrorMessage = "Debe agregar un precio base")]
    public double precioHora {get;set;}
    
    //por defecto
    public bool conIluminacion {get;set;} = false;
    public TemporadaEnum? temporada {get;set;} = TemporadaEnum.MEDIA;
}