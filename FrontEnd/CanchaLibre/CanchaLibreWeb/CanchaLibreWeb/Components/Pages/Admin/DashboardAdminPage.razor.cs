using CanchaLibreWeb.Servicios.Admin;
using CanchaLibreWeb.Servicios.Notificaciones;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class DashboardAdminPage : ComponentBase
{
    [Inject] private IAdminStateService AdminState { get; set; } = default!;
    [Inject] private ReportesIngresosService IngresosService { get; set; } = default!;
    [Inject] private NotificacionService Notificaciones { get; set; } = default!;

    private int totalUsuarios;
    private int totalCanchas;
    private int reservasDelMes;
    private decimal ingresosTotales;

    protected class EstadoReservaItem
    {
        public string Label { get; set; } = string.Empty;
        public string Color { get; set; } = string.Empty;
        public int Cantidad { get; set; }
        public int Altura { get; set; }
    }

    protected List<EstadoReservaItem> DatosReservasPorEstado { get; set; } = new();

    protected class ReservaMesItem
    {
        public string Label { get; set; } = string.Empty;
        public int Cantidad { get; set; }
        public int Altura { get; set; }
    }

    protected List<ReservaMesItem> DatosReservasMensuales { get; set; } = new();

    private bool mostrarToastReserva;
    private string mensajeToastReserva = string.Empty;
    private bool datosCargados;
    private string mensajeError = string.Empty;

    protected override async Task OnInitializedAsync()
    {
        try
        {
            await AdminState.InicializarAsync();

            if (!string.IsNullOrEmpty(AdminState.MensajeError))
            {
                mensajeError = AdminState.MensajeError;
                return;
            }

            var clientes = AdminState.Clientes;
            var propietarios = AdminState.Propietarios;
            var canchas = AdminState.Canchas;
            var reservas = AdminState.Reservas;

            totalUsuarios = clientes.Count + propietarios.Count;
            totalCanchas = canchas.Count;

            var ahora = DateTime.Now;
            reservasDelMes = reservas.Count(r =>
                r.fecha.HasValue &&
                r.fecha.Value.Month == ahora.Month &&
                r.fecha.Value.Year == ahora.Year);

            var ingresosData = IngresosService.ObtenerIngresos();
            ingresosTotales = (decimal)ingresosData.Sum(d => d.Ingresos);

            CargarGraficoReservasPorEstado(reservas);
            CargarGraficoReservasMensuales(reservas, ahora);

            datosCargados = true;
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cargar datos: {ex.Message}";
        }

        Notificaciones.ReservaConfirmada += OnReservaConfirmada;
    }

    private void CargarGraficoReservasPorEstado(List<ReservaViewModel> reservas)
    {
        var grupos = reservas.GroupBy(r => r.estado)
            .ToDictionary(g => g.Key, g => g.Count());

        var items = new List<EstadoReservaItem>
        {
            new() { Label = "Confirmada", Color = "#48BE74", Cantidad = grupos.GetValueOrDefault(EstadoReservaEnum.CONFIRMADA, 0) },
            new() { Label = "Pendiente", Color = "#F59E0B", Cantidad = grupos.GetValueOrDefault(EstadoReservaEnum.PENDIENTE_PAGO, 0) },
            new() { Label = "Cancelada", Color = "#EF4444", Cantidad = grupos.GetValueOrDefault(EstadoReservaEnum.CANCELADA, 0) },
            new() { Label = "Rechazada", Color = "#6B7280", Cantidad = grupos.GetValueOrDefault(EstadoReservaEnum.RECHAZADA, 0) }
        };

        var max = items.Max(i => i.Cantidad);
        if (max < 1) max = 1;
        foreach (var item in items)
            item.Altura = (int)Math.Round((double)item.Cantidad / max * 200);

        DatosReservasPorEstado = items;
    }

    private void CargarGraficoReservasMensuales(List<ReservaViewModel> reservas, DateTime ahora)
    {
        var meses = Enumerable.Range(0, 6)
            .Select(i => new DateTime(ahora.Year, ahora.Month, 1).AddMonths(-i))
            .Reverse()
            .ToList();

        DatosReservasMensuales = meses.Select(m => new ReservaMesItem
        {
            Label = m.ToString("MMM\nyyyy"),
            Cantidad = reservas.Count(r =>
                r.fecha.HasValue &&
                r.fecha.Value.Month == m.Month &&
                r.fecha.Value.Year == m.Year)
        }).ToList();

        var max = DatosReservasMensuales.Max(i => i.Cantidad);
        if (max < 1) max = 1;
        foreach (var item in DatosReservasMensuales)
            item.Altura = (int)Math.Round((double)item.Cantidad / max * 200);
    }

    private async void OnReservaConfirmada(ReservaExitosaInfo info)
    {
        mensajeToastReserva = $"{info.NombreCliente} reservó {info.NombreCancha}.";
        mostrarToastReserva = true;
        await InvokeAsync(StateHasChanged);
        await Task.Delay(10_000);
        mostrarToastReserva = false;
        await InvokeAsync(StateHasChanged);
    }

    public void Dispose()
    {
        Notificaciones.ReservaConfirmada -= OnReservaConfirmada;
    }
}
