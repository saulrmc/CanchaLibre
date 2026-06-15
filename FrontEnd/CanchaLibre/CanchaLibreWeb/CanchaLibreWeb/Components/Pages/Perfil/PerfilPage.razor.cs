using Microsoft.AspNetCore.Components;
using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.Components.Pages.Perfil;

public partial class PerfilPage : ComponentBase
{
    // Modelo para mapear y validar los campos editables del perfil
    protected class CambiarDatosModel
    {
        [Required(ErrorMessage = "El nombre es obligatorio.")]
        public string Nombres { get; set; } = string.Empty;

        [Required(ErrorMessage = "El correo es obligatorio.")]
        [EmailAddress(ErrorMessage = "Formato de correo inválido.")]
        public string Correo { get; set; } = string.Empty;

        [Required(ErrorMessage = "El teléfono es obligatorio.")]
        [RegularExpression(@"^9\d{8}$", ErrorMessage = "El teléfono debe empezar con 9 y tener 9 dígitos.")]
        public string Telefono { get; set; } = string.Empty;

        public string ContrasenaActual { get; set; } = string.Empty;
        public string NuevaContrasena { get; set; } = string.Empty;
    }

    protected CambiarDatosModel Modelo { get; set; } = new();
    
    // Estados de la interfaz
    protected string MensajeExito { get; set; } = string.Empty;
    protected string MensajeError { get; set; } = string.Empty;
    protected string FechaRegistro { get; set; } = "15 de Enero, 2026";
    protected string TipoUsuario { get; set; } = "Cliente Regular";

    protected override void OnInitialized()
    {
        
        Modelo = new CambiarDatosModel
        {
            Nombres = "Carlos Mendoza",
            Correo = "carlos.mendoza@email.com",
            Telefono = "996852763"
        };
    }

    protected void GuardarCambios()
    {
        MensajeExito = string.Empty;
        MensajeError = string.Empty;

        
        if (!string.IsNullOrEmpty(Modelo.NuevaContrasena) && string.IsNullOrEmpty(Modelo.ContrasenaActual))
        {
            MensajeError = "Debe ingresar su contraseña actual para establecer una nueva.";
            return;
        }

        
        // await Http.PutAsJsonAsync($"api/usuarios/{Id}", Modelo);

        MensajeExito = "¡Perfil actualizado correctamente!";
    }
}