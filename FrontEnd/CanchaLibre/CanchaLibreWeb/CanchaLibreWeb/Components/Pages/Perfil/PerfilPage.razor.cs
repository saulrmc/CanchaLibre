using System.Security.Claims;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using System.ComponentModel.DataAnnotations;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Usuarios;

namespace CanchaLibreWeb.Components.Pages.Perfil;

public partial class PerfilPage : ComponentBase
{
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;
    [Inject] private IClientesServiceClient ClientesService { get; set; } = default!;
    [Inject] private IPropietariosServiceClient PropietariosService { get; set; } = default!;
    [Inject] private NavigationManager Nav { get; set; } = default!;

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
    protected string MensajeExito { get; set; } = string.Empty;
    private bool mostrarContrasenaActual = false;
    private bool mostrarNuevaContrasena = false;
    protected string MensajeError { get; set; } = string.Empty;
    protected string TipoUsuario { get; set; } = string.Empty;
    protected bool cargando = true;

    private string _rol = string.Empty;
    private int _userId;
    private ClienteViewModel? _clienteData;
    private PropietarioViewModel? _propietarioData;

    protected override async Task OnInitializedAsync()
    {
        try
        {
            var authState = await AuthStateProvider.GetAuthenticationStateAsync();
            var user = authState.User;

            if (user.Identity is not { IsAuthenticated: true })
            {
                Nav.NavigateTo("/Login");
                return;
            }

            _userId = int.Parse(user.FindFirst("IdUsuario")?.Value ?? "0");
            _rol = user.FindFirst(ClaimTypes.Role)?.Value ?? string.Empty;

            if (_rol.Equals("CLIENTE", StringComparison.OrdinalIgnoreCase))
            {
                _clienteData = await Task.Run(() => ClientesService.Obtener(_userId));
                if (_clienteData != null)
                {
                    Modelo.Nombres = _clienteData.Nombres;
                    Modelo.Correo = _clienteData.Correo;
                    Modelo.Telefono = _clienteData.Telefono;
                }
                TipoUsuario = "Cliente";
            }
            else if (_rol.Equals("PROPIETARIO", StringComparison.OrdinalIgnoreCase))
            {
                _propietarioData = await Task.Run(() => PropietariosService.Obtener(_userId));
                if (_propietarioData != null)
                {
                    Modelo.Nombres = _propietarioData.Nombres;
                    Modelo.Correo = _propietarioData.Correo;
                    Modelo.Telefono = _propietarioData.Telefono;
                }
                TipoUsuario = "Propietario";
            }
            else
            {
                Nav.NavigateTo("/");
            }
        }
        finally
        {
            cargando = false;
        }
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

        try
        {
            if (_rol.Equals("CLIENTE", StringComparison.OrdinalIgnoreCase) && _clienteData != null)
            {
                _clienteData.Nombres = Modelo.Nombres.Trim();
                _clienteData.Correo = Modelo.Correo.Trim();
                _clienteData.Telefono = Modelo.Telefono.Trim();

                if (!string.IsNullOrEmpty(Modelo.NuevaContrasena))
                {
                    _clienteData.Cuenta ??= new CuentaUsuarioViewModel();
                    _clienteData.Cuenta.Password = Modelo.NuevaContrasena;
                }

                ClientesService.Guardar(_clienteData, Estado.Modificado);
            }
            else if (_rol.Equals("PROPIETARIO", StringComparison.OrdinalIgnoreCase) && _propietarioData != null)
            {
                _propietarioData.Nombres = Modelo.Nombres.Trim();
                _propietarioData.Correo = Modelo.Correo.Trim();
                _propietarioData.Telefono = Modelo.Telefono.Trim();

                if (!string.IsNullOrEmpty(Modelo.NuevaContrasena))
                {
                    _propietarioData.Cuenta ??= new CuentaUsuarioViewModel();
                    _propietarioData.Cuenta.Password = Modelo.NuevaContrasena;
                }

                PropietariosService.Guardar(_propietarioData, Estado.Modificado);
            }

            MensajeExito = "¡Perfil actualizado correctamente!";
        }
        catch (Exception ex)
        {
            MensajeError = $"Error al guardar los cambios: {ex.Message}";
        }
    }
}