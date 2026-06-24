using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Usuarios;

namespace CanchaLibreWeb.Components.Pages.Login;

public partial class LoginPage : ComponentBase
{
    [Inject] private NavigationManager Nav { get; set; } = default!;
    [Inject] private IAuthServiceClient AuthService { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;

    // Modelos de solicitud vinculados a la vista
    private SolicitudLoginViewModel LoginModel { get; set; } = new();
    private SolicitudRecuperarContrasenaViewModel RecuperarModel { get; set; } = new();

    // Estado local de la UI
    private bool mostrarRecuperacion = false;
    private string mensajeError = string.Empty;
    private string mensajeStatus = string.Empty;
    private bool esErrorStatus = false;

    private void ProcesarLogin()
    {
        try
        {
            mensajeError = string.Empty;

            // El servicio barre los endpoints de Java de forma automática buscando coincidencia
            var (usuario, rolDetectado) = AuthService.ValidarCredenciales(LoginModel.Correo, LoginModel.Contrasena);

            if (usuario == null || string.IsNullOrEmpty(rolDetectado))
            {
                mensajeError = "El correo electrónico o la contraseña son incorrectos.";
                return;
            }

            // Si se encuentra, marcamos la sesión global con el rol devuelto directamente por la BD
            if (AuthStateProvider is CustomAuthStateProvider customAuthStateProvider)
            {
                customAuthStateProvider.MarcarComoAutenticado(usuario, rolDetectado);
                RedirigirSegunRol(rolDetectado);
            }
        }
        catch (Exception ex)
        {
            mensajeError = $"Error de comunicación con el servidor: {ex.Message}";
        }
    }

    private void ProcesarRecuperacion()
    {
        try
        {
            mensajeStatus = string.Empty;
            
            // Consumo de tu método de verificación sobre listas REST
            bool existeUsuario = AuthService.SolicitarRecuperacion(RecuperarModel.Correo);

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

    private string MapearRolEnumAString(RolEnum rol) => rol switch
    {
        RolEnum.CLIENTE => "Cliente",
        RolEnum.PROPIETARIO => "Propietario",
        RolEnum.ADMINISTRADOR => "Admin",
        _ => "NoAdmitido"
    };

    private void RedirigirSegunRol(string rol)
    {
        switch (rol.ToLower())
        {
            case "admin":
                Nav.NavigateTo("/Admin/Dashboard");
                break;
            case "propietario":
                Nav.NavigateTo("/PortalPropietario/Publicar");
                break;
            case "cliente":
                Nav.NavigateTo("/Portal/MisReservas");
                break;
            default:
                Nav.NavigateTo("/");
                break;
        }
    }
}