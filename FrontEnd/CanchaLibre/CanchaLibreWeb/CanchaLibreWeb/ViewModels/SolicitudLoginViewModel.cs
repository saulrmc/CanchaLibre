using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class SolicitudLoginViewModel {
    [Required(ErrorMessage = "Debe ingresar un correo")]
    [EmailAddress(ErrorMessage = "El correo no tiene un formato valido")]
    public string Correo { get; set; } = string.Empty;

    [Required(ErrorMessage = "Debe ingresar la contraseña")]
    [StringLength(100, ErrorMessage = "La contraseña no puede exceder 100 caracteres")]
    public string Contrasena { get; set; } = string.Empty;
}
