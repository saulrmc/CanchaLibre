using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class PropietarioViewModel : PersonaViewModel
{
    public double Calificacion {get; set;}

    [RegularExpression("^[0-9+\\-()\\s]{11,11}$", ErrorMessage = "El RUC debe tener 11 caracteres validos.")]
    public string Ruc { get; set; } = string.Empty;

    public double Saldo {get; set;}
}