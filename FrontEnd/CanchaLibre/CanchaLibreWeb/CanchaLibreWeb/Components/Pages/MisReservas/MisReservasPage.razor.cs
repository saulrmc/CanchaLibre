using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Reservas;
using CanchaLibreWeb.Servicios.Transacciones;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.Servicios.Usuarios;
using System.Security.Claims;

namespace CanchaLibreWeb.Components.Pages.MisReservas;

public partial class MisReservasPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private IReservasServiceClient ReservasService { get; set; } = default!;
    [Inject] private IComprobantesServiceClient ComprobantesService { get; set; } = default!;
    [Inject] private IPagosServiceClient PagosService { get; set; } = default!;
    [Inject] private IClientesServiceClient ClientesService { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;

    protected List<ReservaViewModel> reservasUsuario { get; set; } = new();
    protected string? mensajeError;

    protected bool mostrarModalDetalle;
    protected ReservaViewModel? reservaDetalle;

    protected bool mostrarPagoEnModal;
    protected MetodoPagoEnum metodoPagoSeleccionado;
    protected bool procesandoPago;
    protected bool pagoExitoso;
    protected string? mensajePago;

    protected override void OnInitialized()
    {
        try
        {
            var authState = AuthStateProvider.GetAuthenticationStateAsync().GetAwaiter().GetResult();
            var user = authState.User;

            if (!user.Identity?.IsAuthenticated ?? true)
            {
                NavigationManager.NavigateTo("/Login", forceLoad: true);
                return;
            }

            var nombreCliente = user.FindFirst(ClaimTypes.Name)?.Value;
            if (string.IsNullOrEmpty(nombreCliente))
            {
                NavigationManager.NavigateTo("/Login", forceLoad: true);
                return;
            }

            var cliente = ClientesService.BuscarPorNombre(nombreCliente);
            if (cliente == null)
            {
                mensajeError = "No se encontró el cliente asociado a su cuenta.";
                return;
            }

            reservasUsuario = ReservasService.ListarPorCliente(cliente.Id);
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cargar las reservas: {ex.Message}";
        }
    }

    protected void VerDetalle(int reservaId)
    {
        reservaDetalle = reservasUsuario.FirstOrDefault(r => r.idReserva == reservaId);
        if (reservaDetalle != null)
        {
            mostrarPagoEnModal = false;
            pagoExitoso = false;
            mensajePago = null;
            mostrarModalDetalle = true;
        }
    }

    protected void CerrarModal()
    {
        mostrarModalDetalle = false;
        mostrarPagoEnModal = false;
        reservaDetalle = null;
        mensajePago = null;
    }

    protected void MostrarPagoEnModal()
    {
        mostrarPagoEnModal = true;
        metodoPagoSeleccionado = MetodoPagoEnum.YAPE;
        mensajePago = null;
    }

    protected void CancelarPagoEnModal()
    {
        mostrarPagoEnModal = false;
        mensajePago = null;
    }

    protected void PagarReservaEnModal()
    {
        if (reservaDetalle == null || reservaDetalle.bloques == null || !reservaDetalle.bloques.Any())
        {
            mensajePago = "No hay bloques seleccionados en la reserva.";
            return;
        }

        procesandoPago = true;
        mensajePago = null;

        try
        {
            var totalBloques = reservaDetalle.bloques.Sum(b => b.precio);
            var totalConComision = totalBloques + 5.00;

            // 1. Crear comprobante
            var comprobante = ComprobantesService.CrearConReserva(reservaDetalle.idReserva, new ComprobanteRestDto
            {
                serie = "B001",
                montoBloques = totalBloques,
                FechaEmision = new DateTime(DateTime.Now.Ticks, DateTimeKind.Unspecified)
            });

            // 2. Crear pago (SP cambia estado a CONFIRMADA automáticamente)
            var pago = PagosService.CrearConReserva(reservaDetalle.idReserva, new PagoRestDto
            {
                metodoPago = metodoPagoSeleccionado.ToString(),
                monto = totalConComision,
                fechaPago = new DateTime(DateTime.Now.Ticks, DateTimeKind.Unspecified)
            });

            // 3. Vincular comprobante al pago
            pago.comprobante = comprobante;
            PagosService.Actualizar(pago.idPago, pago);

            // 4. Recargar reservas
            var authState = AuthStateProvider.GetAuthenticationStateAsync().GetAwaiter().GetResult();
            var nombreCliente = authState.User.FindFirst(ClaimTypes.Name)?.Value;
            if (!string.IsNullOrEmpty(nombreCliente))
            {
                var cliente = ClientesService.BuscarPorNombre(nombreCliente);
                if (cliente != null)
                {
                    reservasUsuario = ReservasService.ListarPorCliente(cliente.Id);
                    reservaDetalle = reservasUsuario.FirstOrDefault(r => r.idReserva == reservaDetalle.idReserva);
                }
            }

            pagoExitoso = true;
            mostrarPagoEnModal = false;
            mensajePago = "Pago realizado con éxito.";
        }
        catch (Exception ex)
        {
            mensajePago = $"Error al procesar el pago: {ex.Message}";
        }
        finally
        {
            procesandoPago = false;
        }
    }

    protected void CancelarReserva(int reservaId)
    {
        try
        {
            ReservasService.Eliminar(reservaId);
            reservasUsuario.RemoveAll(r => r.idReserva == reservaId);
            if (reservaDetalle?.idReserva == reservaId)
            {
                CerrarModal();
            }
            else
            {
                StateHasChanged();
            }
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cancelar la reserva: {ex.Message}";
        }
    }
}
