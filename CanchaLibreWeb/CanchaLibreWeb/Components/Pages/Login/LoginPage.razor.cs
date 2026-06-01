using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Login;

public partial class LoginPage : ComponentBase {
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    [SupplyParameterFromQuery(Name = "returnUrl")]
    public string? ReturnUrl { get; set; }

    [SupplyParameterFromQuery(Name = "error")]
    public string? Error { get; set; }

    private string MensajeError { get; set; } = string.Empty;
    [SupplyParameterFromQuery(Name = "registroExitoso")]
    public string? RegistroExitoso { get; set; }

    private string MensajeInfo { get; set; } = string.Empty;

    [SupplyParameterFromForm(FormName = "LoginForm")]
    public LoginViewModel Modelo { get; set; } = default!;

    protected override void OnInitialized() => Modelo ??= new();

    protected override void OnParametersSet()
    {
       // Si la URL viene con ?error=1 (Datos incorrectos)
        MensajeError = string.Equals(Error, "1", StringComparison.Ordinal)
            ? "No se pudo iniciar sesion. Verifique sus credenciales."
            : string.Empty;

        // Si la URL viene con ?registroExitoso=true (Acaba de registrarse)
        MensajeInfo = string.Equals(RegistroExitoso, "true", StringComparison.OrdinalIgnoreCase)
            ? "¡Cuenta creada con éxito! Ya puede iniciar sesión."
            : string.Empty;
    }

    private void ProcesarLogin()
    {
        // 1. Aquí harías la llamada HTTP a tu API de Java pasando usuario y contraseña.
        // string rolUsuario = await AuthService.Login(usuario, contrasena);
        
        // Simulemos que el backend nos responde con el rol del usuario:
        string rolUsuario = "Propietario"; // Puede ser "Cliente", "Admin", "Empleado"

        // 2. Redirección condicional dinámica
        // Por ahora todos van a Home, pero podrías tener rutas específicas para cada rol si lo deseas

        switch (rolUsuario)
        {
            case "Admin":
                NavigationManager.NavigateTo("/");
                break;
                
            case "Propietario":
                NavigationManager.NavigateTo("/"); // O su panel de canchas
                break;
                
            case "Cliente":
            default:
                NavigationManager.NavigateTo("/"); // La Home clásica para ver y reservar canchas
                break;
        }
        
    }
}
