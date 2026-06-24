using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class CuentaUsuarioViewModel : IValidatableObject {
    public int Id { get; set; }

    [Required(ErrorMessage = "El usuario es obligatorio.")]
    [StringLength(100, ErrorMessage = "El usuario no puede exceder 100 caracteres.")]
    public string UserName { get; set; } = string.Empty;

    [StringLength(100, ErrorMessage = "La contrasena no puede exceder 100 caracteres.")]
    public string Password { get; set; } = string.Empty;

    public bool Activo { get; set; } = true;

    [Required(ErrorMessage = "Debe escoger un rol")]
    public RolEnum Rol {get; set;}
    public int IntentosFallidos {get; set;}
    public DateTime UltimaSesion {get; set;}
    public DateTime FechaBloqueo {get; set;}

    public IEnumerable<ValidationResult> Validate(ValidationContext validationContext) {
        if (Id <= 0 && string.IsNullOrWhiteSpace(Password)) {
            yield return new ValidationResult("La contrasena es obligatoria para registrar una cuenta.", [nameof(Password)]);
        }

        if (!string.IsNullOrWhiteSpace(Password) && Password.Length < 6) {
            yield return new ValidationResult("La contrasena debe tener al menos 6 caracteres.", [nameof(Password)]);
        }

        var passwordInformada = !string.IsNullOrWhiteSpace(Password);

        if (passwordInformada) {
            yield return new ValidationResult("La confirmacion de contrasena no coincide.", [nameof(Password)]);
        }
    }
}