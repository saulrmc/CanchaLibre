using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Components.Pages.Registrar;

public partial class RegistrarPage : ComponentBase
{
    // Variables de control de flujo
    public int PasoActual { get; set; } = 1;
    private bool EsPropietario { get; set; } = false;
    private string MensajeError { get; set; } = string.Empty;

    // Un objeto dummy para cumplir con el requisito de EditForm en Blazor
    private object IdUsuarioFalso { get; set; } = new();

    // Captura temporal de datos de pantalla
    private string NombreInput { get; set; } = string.Empty;
    private string CorreoInput { get; set; } = string.Empty;
    private string ContrasenaInput { get; set; } = string.Empty;
    private string ConfirmarContrasenaInput { get; set; } = string.Empty;
    private string RucInput { get; set; } = string.Empty;
    private string TelefonoInput { get; set; } = string.Empty;

    public void SeleccionarRol(string rol)
    {
        EsPropietario = string.Equals(rol, "Propietario", StringComparison.OrdinalIgnoreCase);
        PasoActual = 2; // Transición automática al formulario base
        MensajeError = string.Empty;
    }

    private void AvanzarPaso2()
    {
        MensajeError = string.Empty;

        // Validación manual de contraseñas idénticas
        if (!string.Equals(ContrasenaInput, ConfirmarContrasenaInput, StringComparison.Ordinal))
        {
            MensajeError = "Las contraseñas no coinciden.";
            return;
        }

        if (EsPropietario)
        {
            PasoActual = 3; // Si es propietario, pide RUC y Teléfono
        }
        else
        {
            FinalizarRegistroCliente(); // Si es cliente, termina aquí
        }
    }

    private void FinalizarRegistroCliente()
    {
        var nuevoCliente = new ClienteViewModel
        {
            Nombres = NombreInput,
            Correo = CorreoInput
        };
        var nuevaCuenta = new CuentaUsuarioViewModel
        {
            Password = ContrasenaInput
        };
        nuevoCliente.Cuenta = nuevaCuenta;

        // Aquí envías 'nuevoCliente' a tu servicio HTTP que conecta con Java
        Console.WriteLine($"Cliente registrado: {nuevoCliente.Nombres} - {nuevoCliente.Correo}");
    }

    private void FinalizarRegistroPropietario()
    {
        MensajeError = string.Empty;

        // Si necesitas agregar validaciones de longitud de RUC o formato puedes hacerlo aquí
        var nuevoPropietario = new PropietarioViewModel
        {
            Nombres = NombreInput,
            Correo = CorreoInput,
            Telefono = TelefonoInput
            // Ruc = RucInput // Descomenta esto cuando agregues la propiedad a tu ViewModel
        };
        var nuevaCuenta = new CuentaUsuarioViewModel
        {
            Password = ContrasenaInput
        };
        nuevoPropietario.Cuenta = nuevaCuenta;

        // Aquí envías 'nuevoPropietario' a tu backend en Java
        Console.WriteLine($"Propietario registrado: {nuevoPropietario.Nombres} - Tel: {nuevoPropietario.Telefono}");
    }
}