using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class LoginViewModel
{
    [Required(ErrorMessage = "Debe ingresar su correo electrónico")]
    [EmailAddress(ErrorMessage = "El formato del correo no es válido")]
    public string Usuario { get; set; } = string.Empty;

    [Required(ErrorMessage = "Debe ingresar su contraseña")]
    public string Contrasena { get; set; } = string.Empty;
}