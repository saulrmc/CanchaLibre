using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Usuarios;
using CanchaLibreWeb.Servicios.Base;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarPage : ComponentBase
{
    [Inject] private NavigationManager Nav { get; set; } = default!;
    [Inject] private IClientesServiceClient ClientesService { get; set; } = default!;
    [Inject] private IPropietariosServiceClient PropietariosService { get; set; } = default!;

    private int pasoActual = 1;
    private string rolSeleccionado = string.Empty;
    private string mensajeError = string.Empty;

    // Instanciamos los ViewModels asegurando que sus objetos internos "Cuenta" no sean nulos
    private ClienteViewModel ClienteModel { get; set; } = new() { Cuenta = new CuentaUsuarioViewModel { Rol = RolEnum.CLIENTE } };
    private PropietarioViewModel PropietarioModel { get; set; } = new() { Cuenta = new CuentaUsuarioViewModel { Rol = RolEnum.PROPIETARIO } };

    private void SeleccionarRol(string rol)
    {
        rolSeleccionado = rol;
        pasoActual = 2;
        mensajeError = string.Empty;
    }

    private void VolverAlPaso1()
    {
        pasoActual = 1;
        rolSeleccionado = string.Empty;
        mensajeError = string.Empty;
        // Reiniciamos los modelos para limpiar el estado anterior
        ClienteModel = new() { Cuenta = new CuentaUsuarioViewModel { Rol = RolEnum.CLIENTE } };
        PropietarioModel = new() { Cuenta = new CuentaUsuarioViewModel { Rol = RolEnum.PROPIETARIO } };
    }

    private void RegistrarCliente()
    {
        try
        {
            mensajeError = string.Empty;
            
            // Consumo directo de tu ClientesServiceRestClient inyectado
            ClientesService.Guardar(ClienteModel, Estado.Nuevo);
            
            // Una vez registrado con éxito en Java, lo mandamos al Login para que inicie sesión limpiamente
            Nav.NavigateTo("/Login");
        }
        catch (Exception ex)
        {
            mensajeError = $"No se pudo completar el registro: {ex.Message}";
        }
    }

    private void RegistrarPropietario()
    {
        try
        {
            mensajeError = string.Empty;

            // Consumo directo de tu PropietariosServiceRestClient inyectado
            PropietariosService.Guardar(PropietarioModel, Estado.Nuevo);

            Nav.NavigateTo("/Login");
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al registrar la cuenta de propietario: {ex.Message}";
        }
    }
}