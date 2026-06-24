using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class PropietarioViewModel : PersonaViewModel
{
    public double Calificacion {get; set;}

    [Required(ErrorMessage = "Debe ingresar su RUC")]
    public string Ruc { get; set; } = string.Empty;
    
}