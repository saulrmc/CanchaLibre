using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarPropietario2Page : ComponentBase
{
    [Inject] public NavigationManager Nav { get; set; } = default!;
    [SupplyParameterFromQuery] public string? Nombre { get; set; }
    [SupplyParameterFromQuery] public string? Correo { get; set; }
    [SupplyParameterFromQuery] public string? Pass { get; set; }

    public PropietarioViewModel Modelo { get; set; } = default!;
    public string RucInput { get; set; } = string.Empty;
    public string TelefonoInput { get; set; } = string.Empty;
    private string MensajeError { get; set; } = string.Empty;
    protected override void OnInitialized() => Modelo ??= new();

    private void FinalizarRegistro()
    {
        if (!string.IsNullOrEmpty(RucInput) && RucInput.Length != 10)
        {
            MensajeError = "El RUC debe tener exactamente 10 dígitos.";
            return;
        }
        if(!string.IsNullOrEmpty(TelefonoInput) && TelefonoInput.Length != 9)
        {
            MensajeError = "El teléfono debe tener exactamente 9 dígitos.";
            return;
        }
        // REconstrucción del objeto completo con los datos que venían de la URL y los del formulario
        Modelo.Nombres = Nombre ?? string.Empty;
        Modelo.Correo = Correo ?? string.Empty;
        Modelo.Contrasena = Pass ?? string.Empty;
        // Modelo.Ruc = RucInput;  Descomentar cuando se actualice el ViewModel

        Nav.NavigateTo($"/");
    }
    
}