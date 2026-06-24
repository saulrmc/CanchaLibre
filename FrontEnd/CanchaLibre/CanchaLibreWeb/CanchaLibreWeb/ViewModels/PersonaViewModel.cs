using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class PersonaViewModel
{
    public int Id {get; set;}

    [Required(ErrorMessage = "Debe ingresar un nombre")]
    public String Nombres { get; set; } = string.Empty;

    [Required(ErrorMessage = "Debe ingresar una cuenta")]
    public CuentaUsuarioViewModel? Cuenta {get; set;} 

    [Required(ErrorMessage = "Debe ingresar un correo")]
    public String Correo { get; set; } = string.Empty;

    [RegularExpression("^[0-9+\\-()\\s]{9,9}$", ErrorMessage = "El telefono debe tener 9 caracteres validos.")]
    public String Telefono { get; set; } = string.Empty;
    public bool Activo { get; set; } = true;
    
}