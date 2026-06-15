using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;
using System.Web;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarPropietarioPage : ComponentBase
{
    [Inject] public NavigationManager Nav { get; set; } = default!;

    public PropietarioViewModel Modelo { get; set; } = default!;
    public string ConfirmarContrasena { get; set; } = string.Empty;
    protected override void OnInitialized() => Modelo ??= new();
    private string MensajeError { get; set; } = string.Empty;

    private void AvanzarPaso()
    {
        if (!string.Equals(Modelo.Contrasena, ConfirmarContrasena, StringComparison.Ordinal))
        {
            MensajeError = "Las contraseñas no coinciden.";
            return;
        }

        // Pasamos los datos recolectados por QueryString a la pantalla final de Propietario
        var query = $"?nombre={HttpUtility.UrlEncode(Modelo.Nombres)}&correo={HttpUtility.UrlEncode(Modelo.Correo)}&pass={HttpUtility.UrlEncode(Modelo.Contrasena)}";
        Nav.NavigateTo($"/Registrar/Propietario/Detalles");
    }
}