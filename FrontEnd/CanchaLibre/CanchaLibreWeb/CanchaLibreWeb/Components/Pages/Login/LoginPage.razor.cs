using CanchaLibreWeb.Servicios.Cuentas;
using CanchaLibreWeb.Servicios.Usuarios;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;

namespace CanchaLibreWeb.Components.Pages.Login;

public partial class LoginPage : ComponentBase {
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private ICuentasUsuarioServiceClient CuentasClient { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthProvider { get; set; } = default!;

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
        MensajeError = string.Empty;

        var usuario = CuentasClient.Login(Modelo.Usuario, Modelo.Contrasena);

        if (usuario == null)
        {
            MensajeError = "Usuario o contraseña incorrectos.";
            return;
        }

        if (AuthProvider is CustomAuthStateProvider customAuth)
        {
            customAuth.MarcarComoAutenticado(usuario, usuario.Rol);
        }

        NavigationManager.NavigateTo(ReturnUrl ?? "/", forceLoad: true);
    }
}
