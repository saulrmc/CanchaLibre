using CanchaLibreWeb.Servicios.Admin;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class CanchasAdminPage : ComponentBase
{
    [Inject] private IAdminStateService AdminState { get; set; } = default!;
    [Inject] private ReportesIngresosService IngresosService { get; set; } = default!;

    protected class ReporteSedeRow
    {
        public int Id { get; set; }
        public string Sede { get; set; } = string.Empty;
        public int Reservas { get; set; }
        public decimal Ingresos { get; set; }
        public int HorasOcupacion { get; set; }
    }

    private List<ReporteSedeRow> _reportesMaster = new();
    protected List<ReporteSedeRow> ReportesFiltrados { get; set; } = new();

    protected string PeriodoSeleccionado { get; set; } = "Último mes";
    protected string FiltrarPorSeleccionado { get; set; } = "Mayores reservas";
    protected string UrlReporteCanchas =>
        $"{AdminState.ReportesPdfBaseUrl}canchas";

    private bool datosCargados;
    private decimal utilidadNeta;
    private double ocupacionPromedioHoras;
    private int actividadReciente;
    private int bloqueados;
    private string mensajeError = string.Empty;

    private int _dropdownOpenId = 0;

    private CanchaViewModel? _canchaSeleccionada;
    private List<ReservaViewModel> _reservasDeCancha = new();
    private bool _showDetalle;
    private bool _showReservas;

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

            var ahora = DateTime.Now;
            var hace30Dias = ahora.AddDays(-30);

            reservas = reservas.Where(r => r is not null).ToList();
            canchas = canchas.Where(c => c is not null).ToList();

            // Ingresos desde endpoint plano (evita problemas de serialización JSON con objetos anidados)
            var ingresosData = IngresosService.ObtenerIngresos();

            // Cards
            utilidadNeta = (decimal)ingresosData.Sum(d => d.Ingresos);

            var confirmadas = reservas.Where(r => r.estado == EstadoReservaEnum.CONFIRMADA).ToList();
            var horasPorReserva = confirmadas.Select(r => CalcularHorasReserva(r)).ToList();
            ocupacionPromedioHoras = horasPorReserva.Count > 0
                ? horasPorReserva.Average()
                : 0;

            var todosUsuarios = new List<PersonaViewModel>();
            todosUsuarios.AddRange(clientes.Where(c => c is not null));
            todosUsuarios.AddRange(propietarios.Where(p => p is not null));

            actividadReciente = todosUsuarios.Count(u =>
                u is not null && u.Cuenta?.UltimaSesion >= hace30Dias);

            bloqueados = todosUsuarios.Count(u =>
                u is not null && (u.Cuenta?.FechaBloqueo > DateTime.MinValue ||
                (u.Cuenta?.IntentosFallidos ?? 0) >= 3));

            // Table rows desde el endpoint de ingresos (datos reales de BD, no calculados desde frontend)
            _reportesMaster = ingresosData.Select(d =>
            {
                var cancha = canchas.FirstOrDefault(c => c.id == d.Id);
                return new ReporteSedeRow
                {
                    Id = d.Id,
                    Sede = cancha is not null && !string.IsNullOrEmpty(cancha.distrito)
                        ? $"{d.Nombre} - {cancha.distrito}"
                        : d.Nombre,
                    Reservas = d.Reservas,
                    Ingresos = (decimal)d.Ingresos,
                    HorasOcupacion = d.HorasOcupacion
                };
            }).ToList();

            datosCargados = true;
            ProcesarFiltrosYOrden();
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al cargar canchas: {ex.Message}";
        }
    }

    private static string FormatHoras(double horas)
    {
        var totalMinutos = (int)Math.Round(horas * 60);
        var h = totalMinutos / 60;
        var m = totalMinutos % 60;
        return $"{h:D2}:{m:D2} hrs";
    }

    private static double CalcularHorasReserva(ReservaViewModel r)
    {
        if (r.bloques == null || r.bloques.Count == 0) return 0;
        return r.bloques.Sum(b => (b.horaFin - b.horaInicio).TotalHours);
    }

    protected void ProcesarFiltrosYOrden()
    {
        var consulta = _reportesMaster.AsEnumerable();

        if (FiltrarPorSeleccionado == "Mayores reservas")
            consulta = consulta.OrderByDescending(r => r.Reservas);
        else if (FiltrarPorSeleccionado == "Mayores ingresos")
            consulta = consulta.OrderByDescending(r => r.Ingresos);
        else if (FiltrarPorSeleccionado == "Más horas ocupadas")
            consulta = consulta.OrderByDescending(r => r.HorasOcupacion);

        ReportesFiltrados = consulta.ToList();
    }

    private void ToggleDropdown(int id)
    {
        _dropdownOpenId = _dropdownOpenId == id ? 0 : id;
    }

    private void VerDetalle(ReporteSedeRow fila)
    {
        _canchaSeleccionada = AdminState.Canchas.FirstOrDefault(c => c.id == fila.Id);
        _showDetalle = true;
        _showReservas = false;
    }

    private void VerReservas(ReporteSedeRow fila)
    {
        _canchaSeleccionada = AdminState.Canchas.FirstOrDefault(c => c.id == fila.Id);
        _reservasDeCancha = AdminState.Reservas
            .Where(r => r.cancha?.id == fila.Id)
            .OrderByDescending(r => r.fecha)
            .ToList();
        _showReservas = true;
        _showDetalle = false;
    }

    private void CerrarModal()
    {
        _showDetalle = false;
        _showReservas = false;
        _canchaSeleccionada = null;
    }

    private static string FormatFecha(DateTime? fecha)
    {
        return fecha?.ToString("dd/MM/yyyy") ?? "-";
    }

    private static string FormatoEstadoBadge(EstadoReservaEnum estado)
    {
        return estado switch
        {
            EstadoReservaEnum.CONFIRMADA => "bg-success-subtle text-success",
            EstadoReservaEnum.PENDIENTE_PAGO => "bg-warning-subtle text-warning",
            EstadoReservaEnum.CANCELADA => "bg-danger-subtle text-danger",
            EstadoReservaEnum.RECHAZADA => "bg-danger-subtle text-danger",
            _ => "bg-light text-dark"
        };
    }

    private static string FormatoEstadoTexto(EstadoReservaEnum estado)
    {
        return estado switch
        {
            EstadoReservaEnum.CONFIRMADA => "Confirmada",
            EstadoReservaEnum.PENDIENTE_PAGO => "Pendiente",
            EstadoReservaEnum.CANCELADA => "Cancelada",
            EstadoReservaEnum.RECHAZADA => "Rechazada",
            _ => estado.ToString()
        };
    }
}
