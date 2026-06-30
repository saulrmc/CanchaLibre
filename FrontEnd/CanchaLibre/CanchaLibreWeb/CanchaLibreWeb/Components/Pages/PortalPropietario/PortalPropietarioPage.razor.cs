using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.Servicios.Reservas;
using System.Security.Claims;

namespace CanchaLibreWeb.Components.Pages.PortalPropietario;

public partial class PortalPropietarioPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;
    [Inject] private ICanchasServiceClient CanchasService { get; set; } = default!;
    [Inject] private IReservasServiceClient ReservasService { get; set; } = default!;

    protected int BusquedasTotales { get; set; } = 64;

    protected decimal UtilidadNeta { get; set; }

    protected int ReservasMes { get; set; }

    protected decimal IngresosTotales { get; set; }

    protected int PorcentajeCompletado { get; set; }
    protected decimal MontoCobrado { get; set; }
    protected decimal MontoPendiente { get; set; }

    protected List<ReservaReciente> ReservasRecientes { get; set; } = new();
    protected bool cargando = true;
    protected string? mensajeError;

    protected override async Task OnInitializedAsync()
    {
        try
        {
            var authState = await AuthStateProvider.GetAuthenticationStateAsync();
            var user = authState.User;

            if (!user.Identity?.IsAuthenticated ?? true)
            {
                NavigationManager.NavigateTo("/Login", forceLoad: true);
                return;
            }

            var userName = user.FindFirst("Username")?.Value;
            if (string.IsNullOrEmpty(userName))
            {
                NavigationManager.NavigateTo("/Login", forceLoad: true);
                return;
            }

            var canchas = await Task.Run(() => CanchasService.ListarPorPropietario(userName));

            var todasReservas = new List<ReservaViewModel>();
            foreach (var cancha in canchas)
            {
                var reservas = await Task.Run(() => ReservasService.ListarPorCancha(cancha.id));
                todasReservas.AddRange(reservas);
            }

            var now = DateTime.Now;
            var reservasMesActual = todasReservas.Where(r =>
                r.fecha.HasValue && r.fecha.Value.Year == now.Year && r.fecha.Value.Month == now.Month).ToList();

            ReservasMes = reservasMesActual.Count;
            var cobradas = todasReservas
                .Where(r => r.estado == EstadoReservaEnum.CONFIRMADA && r.bloques != null && r.bloques.Any())
                .ToList();
            var pendientes = todasReservas
                .Where(r => r.estado == EstadoReservaEnum.PENDIENTE_PAGO && r.bloques != null && r.bloques.Any())
                .ToList();

            IngresosTotales = (decimal)cobradas.Sum(r => r.bloques!.Sum(b => b.precio));
            var comisionTotal = cobradas.Count * 5m;
            UtilidadNeta = Math.Max(0, IngresosTotales - comisionTotal);

            MontoCobrado = (decimal)cobradas.Sum(r => r.bloques!.Sum(b => b.precio));
            MontoPendiente = (decimal)pendientes.Sum(r => r.bloques!.Sum(b => b.precio));

            var totalConEstado = cobradas.Count + pendientes.Count;
            PorcentajeCompletado = totalConEstado > 0
                ? (int)Math.Round((double)cobradas.Count / totalConEstado * 100)
                : 0;

            ReservasRecientes = todasReservas
                .Where(r => r.cancha != null)
                .OrderByDescending(r => r.fecha)
                .Take(10)
                .Select(r => new ReservaReciente
                {
                    ComplejoDeportivo = r.cancha?.nombre ?? "Sin nombre",
                    Fecha = r.fecha ?? DateTime.MinValue,
                    HoraInicio = r.bloques?.FirstOrDefault()?.horaInicio.ToString() ?? "",
                    HoraFin = r.bloques?.LastOrDefault()?.horaFin.ToString() ?? "",
                    Estado = r.estado == EstadoReservaEnum.CONFIRMADA ? "Cobrado" : "Por cobrar"
                }).ToList();
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cargar el dashboard: {ex.Message}";
        }
        finally
        {
            cargando = false;
        }
    }
    protected string FormatearMonto(decimal monto)
    {
        if (monto >= 1000m)
            return $"s/. {monto:N2} mil";
        return $"s/. {monto:N2}";
    }
}

public class ReservaReciente
{
    public string ComplejoDeportivo { get; set; } = string.Empty;
    public DateTime Fecha { get; set; }
    public string HoraInicio { get; set; } = string.Empty;
    public string HoraFin { get; set; } = string.Empty;
    public string Estado { get; set; } = string.Empty;
    
}