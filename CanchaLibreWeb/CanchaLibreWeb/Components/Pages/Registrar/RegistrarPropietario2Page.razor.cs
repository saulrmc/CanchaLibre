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
    protected override void OnInitialized() => Modelo ??= new();

    private void FinalizarRegistro()
    {
        // Reconstruimos el objeto completo con los datos que venían de la URL y los del formulario
        Modelo.Nombres = Nombre ?? string.Empty;
        Modelo.Correo = Correo ?? string.Empty;
        Modelo.Contrasena = Pass ?? string.Empty;
        // Modelo.Ruc = RucInput;  Descomentar cuando se actualice el ViewModel

        Nav.NavigateTo("/Login?registroExitoso=true");
    }
    
}