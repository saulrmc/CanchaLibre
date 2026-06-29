using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Reservas;
using CanchaLibreWeb.Servicios.Transacciones;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
using CanchaLibreWeb.Components.Pages.CanchaView;

namespace CanchaLibreWeb.Components.Pages.Reservas;

public partial class ReservaConfirmadaPage : ComponentBase
{
    [Parameter] public int Id { get; set; }
    [SupplyParameterFromQuery] public string? Fecha { get; set; }
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private IReservasServiceClient ReservasService { get; set; } = default!;
    [Inject] private IComprobantesServiceClient ComprobantesService { get; set; } = default!;
    [Inject] private IPagosServiceClient PagosService { get; set; } = default!;

    private ReservaViewModel? reserva;
    private string mensajeError = string.Empty;

    private bool mostrarSelectorPago;
    private MetodoPagoEnum metodoPagoSeleccionado;
    private bool procesandoPago;
    private bool pagoExitoso;
    private string? mensajePago;

    private string NombreCancha => reserva?.cancha?.nombre ?? "---";
    private string UbicacionCancha => reserva?.cancha?.direccion ?? "---";
    private string CodigoReserva => $"RES-{reserva?.idReserva ?? 0:D6}";

    private string FechaFormateada
    {
        get
        {
            if (!string.IsNullOrEmpty(Fecha) && DateTime.TryParse(Fecha, out var dt))
                return dt.ToString("dddd, dd 'de' MMMM yyyy", new System.Globalization.CultureInfo("es-PE"));
            var bloque = reserva?.bloques?.FirstOrDefault();
            if (bloque != null)
                return $"Próximo {bloque.diaSemana.ToString().ToLowerCapitalized()}";
            return "---";
        }
    }

    private string HoraRango
    {
        get
        {
            var bloques = reserva?.bloques;
            if (bloques == null || !bloques.Any()) return "---";
            var minInicio = bloques.Min(b => b.horaInicio);
            var maxFin = bloques.Max(b => b.horaFin);
            return $"{minInicio:HH:mm} - {maxFin:HH:mm} hrs";
        }
    }

    private string Duracion
    {
        get
        {
            var cant = reserva?.bloques?.Count ?? 0;
            return cant switch
            {
                0 => "---",
                1 => "1 hora",
                _ => $"{cant} horas"
            };
        }
    }

    private bool mostrarToast = false;
    public class ItemPago
    {
        public string Concepto { get; set; } = string.Empty;
        public double Monto { get; set; }
    }

    private List<ItemPago> itemsPago
    {
        get
        {
            var list = new List<ItemPago>();
            if (reserva?.bloques != null && reserva.bloques.Any())
            {
                var totalCancha = reserva.bloques.Sum(b => b.precio);
                list.Add(new ItemPago { Concepto = "Alquiler de cancha", Monto = totalCancha });
                list.Add(new ItemPago { Concepto = "Costo de servicio", Monto = 5.00 });
            }
            return list;
        }
    }

    private double Total => itemsPago.Sum(i => i.Monto);
    private double TotalBloques => reserva?.bloques?.Sum(b => b.precio) ?? 0;

    protected override void OnInitialized()
    {
        CargarReserva();
    }

    private void CargarReserva()
    {
        reserva = ReservasService.Obtener(Id);
        if (reserva == null)
            mensajeError = "No se pudo cargar la información de la reserva.";
    }

    private void MostrarPago()
    {
        mostrarSelectorPago = true;
        metodoPagoSeleccionado = MetodoPagoEnum.YAPE;
    }

    private void CancelarPago()
    {
        mostrarSelectorPago = false;
        mensajePago = null;
    }

    private void PagarReserva()
    {
        if (reserva == null || reserva.bloques == null || !reserva.bloques.Any())
        {
            mensajePago = "No hay bloques seleccionados en la reserva.";
            return;
        }

        procesandoPago = true;
        mensajePago = null;

        try
        {
            // 1. Crear comprobante
            var comprobante = ComprobantesService.CrearConReserva(reserva.idReserva, new ComprobanteRestDto
            {
                serie = "B001",
                montoBloques = TotalBloques,
                FechaEmision = new DateTime(DateTime.Now.Ticks, DateTimeKind.Unspecified)
            });

            // 2. Crear pago (el SP ya cambia la reserva a CONFIRMADA automáticamente)
            var pago = PagosService.CrearConReserva(reserva.idReserva, new PagoRestDto
            {
                metodoPago = metodoPagoSeleccionado.ToString(),
                monto = Total,
                fechaPago = new DateTime(DateTime.Now.Ticks, DateTimeKind.Unspecified)
            });

            // 3. Vincular comprobante al pago
            pago.comprobante = comprobante;
            PagosService.Actualizar(pago.idPago, pago);

            // 4. Recargar reserva actualizada
            CargarReserva();

            pagoExitoso = true;
            mostrarSelectorPago = false;
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

    private void VolverInicio() => NavigationManager.NavigateTo("/");

}