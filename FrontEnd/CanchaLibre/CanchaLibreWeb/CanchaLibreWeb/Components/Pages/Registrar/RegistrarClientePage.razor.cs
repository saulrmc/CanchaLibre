using CanchaLibreWeb.Servicios.Usuarios;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarClientePage : ComponentBase
{
    [Inject] public NavigationManager Nav { get; set; } = default!;
    [Inject] public IClientesServiceClient ClientesClient { get; set; } = default!;

    public ClienteViewModel Modelo { get; set; } = new();

    public string ConfirmarContrasena { get; set; } = string.Empty;

    private string MensajeError { get; set; } = string.Empty;

    private void ProcesarRegistro()
    {
        if (!string.Equals(Modelo.cuenta.Password, ConfirmarContrasena, StringComparison.Ordinal))
        {
            MensajeError = "Las contraseñas no coinciden.";
            return;
        }
        if (string.IsNullOrWhiteSpace(Modelo.Telefono))
        {
            MensajeError = "El teléfono es obligatorio.";
            return;
        }
        try
        {
            MensajeError = string.Empty;

            ClientesClient.Guardar(Modelo, Estado.Nuevo);

            Nav.NavigateTo("/Login?registroExitoso=true");
        }
        catch (Exception ex)
        {
            MensajeError = ex.Message;
            Console.WriteLine(ex.ToString());
        }
    }
}
