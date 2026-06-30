using CanchaLibreWeb.Servicios.Seguridad;
using CanchaLibreWeb.Servicios.Usuarios;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;

namespace CanchaLibreWeb.Components.Pages.Login;

public partial class LoginPage : ComponentBase
{
    [Inject] private NavigationManager Nav { get; set; } = default!;
    [Inject] private IAuthServiceClient AuthService { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;
    [Inject] private ControlIntentosLoginService ControlIntentos { get; set; } = default!;//

    private bool mostrarBloqueo = false;//
    private int segundosBloqueo = 0;//
    private string horaDesbloqueo = string.Empty;

    private SolicitudLoginViewModel LoginModel { get; set; } = new();
    private SolicitudRecuperarContrasenaViewModel RecuperarModel { get; set; } = new();

    private bool mostrarRecuperacion = false;
    private bool mostrarContrasena = false;
    private string mensajeError = string.Empty;
    private string mensajeStatus = string.Empty;
    private bool esErrorStatus = false;

    private async Task ProcesarLogin()
    {
        try
        {
            mensajeError = string.Empty;

            var (id, nombres, correo, rolDetectado, cuentaBloqueada, segundosBloqueoResp) = await Task.Run(() =>
                AuthService.ValidarCredenciales(LoginModel.Correo, LoginModel.Contrasena));

            if (cuentaBloqueada)
            {
                segundosBloqueo = segundosBloqueoResp;
                horaDesbloqueo = DateTime.Now.AddSeconds(segundosBloqueoResp).ToString("HH:mm");
                mostrarBloqueo = true;
                return;
            }

            if (string.IsNullOrEmpty(rolDetectado))
            {
                ControlIntentos.RegistrarFallo(LoginModel.Correo);

                if (ControlIntentos.EstaBloqueado(LoginModel.Correo, out var segundosTrasFallo))
                {
                    segundosBloqueo = segundosTrasFallo;
                    horaDesbloqueo = DateTime.Now.AddSeconds(segundosTrasFallo).ToString("HH:mm");
                    mostrarBloqueo = true;
                    return;
                }

                mensajeError = "El correo electrónico o la contraseña son incorrectos.";
                return;
            }

            ControlIntentos.RegistrarExito(LoginModel.Correo);

            if (AuthStateProvider is CustomAuthStateProvider customAuthStateProvider)
            {
                customAuthStateProvider.MarcarComoAutenticado(id, nombres, correo, rolDetectado, LoginModel.Correo);
                RedirigirSegunRol(rolDetectado);
            }
        }
        catch (Exception ex)
        {
            mensajeError = $"Error de comunicación con el servidor: {ex.Message}";
        }
    }

    private async Task ProcesarRecuperacion()
    {
        try
        {
            mensajeStatus = string.Empty;

            bool existeUsuario = await Task.Run(() => AuthService.SolicitarRecuperacion(RecuperarModel.Correo));

            if (existeUsuario)
            {
                esErrorStatus = false;
                mensajeStatus = "Usuario confirmado en el sistema. (Simulación de envío de correo de restablecimiento realizada)";
            }
            else
            {
                esErrorStatus = true;
                mensajeStatus = "El correo ingresado no pertenece a ningún usuario registrado.";
            }
        }
        catch (Exception ex)
        {
            esErrorStatus = true;
            mensajeStatus = $"Ocurrió un error en el servidor: {ex.Message}";
        }
    }

    private void AlternarVista(bool verRecuperacion)
    {
        mostrarRecuperacion = verRecuperacion;
        mensajeError = string.Empty;
        mensajeStatus = string.Empty;
        LoginModel = new();
        RecuperarModel = new();
    }

    private void RedirigirSegunRol(string rol)
    {
        switch (rol.ToLower())
        {
            case "admin":
                Nav.NavigateTo("/Admin/Dashboard");
                break;
            case "propietario":
                Nav.NavigateTo("/PortalPropietario");
                break;
            case "cliente":
                Nav.NavigateTo("/Portal/MisReservas");
                break;
            default:
                Nav.NavigateTo("/");
                break;
        }
        Nav.NavigateTo("/");
    }
}
