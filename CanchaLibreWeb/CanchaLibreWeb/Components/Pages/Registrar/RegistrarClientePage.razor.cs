using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarClientePage : ComponentBase
{
    [Inject] public NavigationManager Nav { get; set; } = default!;
    
    public ClienteViewModel Modelo { get; set; } = default!;

    public string ConfirmarContrasena { get; set; } = string.Empty;

    protected override void OnInitialized() => Modelo ??= new();

    private string MensajeError { get; set; } = string.Empty;

    private void ProcesarRegistro()
    {
        if (!string.Equals(Modelo.Contrasena, ConfirmarContrasena, StringComparison.Ordinal))
        {
            MensajeError = "Las contraseñas no coinciden.";
            return;
        }

        MensajeError = string.Empty;
        // Aquí llamas a tu HttpClient hacia Java enviando 'Modelo'
        Console.WriteLine($"Enviando cliente a Java: {Modelo.Nombres}");
        
    }
    private void FinalizarRegistroCliente()
    {
        // 1. Envías los datos a Java...
        // var resultado = await Http.PostAsJsonAsync("api/clientes", Modelo);

        // 2. Si Java responde OK, lo mandamos al Login
        // Podemos pasar un parámetro por URL para avisarle a la pantalla de Login que muestre un mensaje bonito
        Nav.NavigateTo("/Login?registroExitoso=true");
    }
}