using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class SolicitudRecuperarContrasenaViewModel {
    
    [EmailAddress(ErrorMessage = "El correo no tiene un formato valido")]
    public string Correo { get; set; } = string.Empty;
}