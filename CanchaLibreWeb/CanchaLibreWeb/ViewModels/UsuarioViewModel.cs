using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public abstract class UsuarioViewModel
{
    public int Id {get; set;}

    [Required(ErrorMessage = "Debe ingresar un nombre")]
    public String Nombres { get; set; } = string.Empty;

    [Required(ErrorMessage = "Debe ingresar una constraseña")]
    public String Contrasena { get; set; } = string.Empty;

    [Required(ErrorMessage = "Debe ingresar un correo")]
    public String Correo { get; set; } = string.Empty;

    [RegularExpression("^[0-9+\\-()\\s]{7,20}$", ErrorMessage = "El telefono debe tener entre 7 y 20 caracteres validos.")]
    public String Telefono { get; set; } = string.Empty;
    public int IntentosFallidos {get;}
    public DateTime UltimaSesion {get; set;}
    
}