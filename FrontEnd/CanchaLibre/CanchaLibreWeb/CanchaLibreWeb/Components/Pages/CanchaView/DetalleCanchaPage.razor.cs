using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Authorization;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.Servicios.Reservas;
using CanchaLibreWeb.Servicios.Usuarios;
using System.Security.Claims;

namespace CanchaLibreWeb.Components.Pages.CanchaView;

public partial class DetalleCanchaPage : ComponentBase
{
    [Parameter] public int Id { get; set; }
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private ICanchasServiceClient CanchasService { get; set; } = default!;
    [Inject] private IReservasServiceClient ReservasService { get; set; } = default!;
    [Inject] private IClientesServiceClient ClientesService { get; set; } = default!;
    [Inject] private AuthenticationStateProvider AuthStateProvider { get; set; } = default!;

    private CanchaViewModel? cancha;
    private bool esSoloLectura = false;
    private List<BloqueHorarioViewModel> bloquesSeleccionados = new();
    private DateTime fechaSeleccionada = DateTime.Today;
    private string? mensajeError;

    // Propiedad calculada para capturar el cambio de fecha y forzar el refresco de horarios
    private DateTime FechaSeleccionada
    {
        get => fechaSeleccionada;
        set
        {
            if (fechaSeleccionada != value)
            {
                fechaSeleccionada = value;
                bloquesSeleccionados.Clear(); // Reiniciar selección al cambiar de fecha
            }
        }
    }

    // Obtiene la lista de bloques que coinciden matemáticamente con el día de la semana seleccionado
    private IEnumerable<BloqueHorarioViewModel> BloquesFiltrados
    {
        get
        {
            if (cancha?.bloques == null) return Enumerable.Empty<BloqueHorarioViewModel>();
            var diaBuscado = ConvertirADiaSemanaEnum(fechaSeleccionada.DayOfWeek);
            return cancha.bloques.Where(b => b.diaSemana == diaBuscado);
        }
    }

    private string DiaSeleccionadoTexto => ConvertirADiaSemanaEnum(fechaSeleccionada.DayOfWeek).ToString().ToLowerCapitalized();

    // Cálculos económicos dinámicos
    private double PrecioSubtotal => bloquesSeleccionados.Sum(b => b.precio);
    private double CostoServicio => bloquesSeleccionados.Any() ? 5.0 : 0; // Comisión fija si selecciona horario
    private double PrecioTotal => PrecioSubtotal + CostoServicio;

    private List<ComentarioItem> comentarios = new()
    {
        new("Juan Sánchez", "La iluminación es excelente."),
        new("Ricardo Carrasco", "Muy bien cuidado el pasto."),
        new("Carlos García", "Muy puntuales con la entrega de chalecos.")
    };

    protected string DescripcionCorta { get; set; } = string.Empty;
    protected List<string> ReglasSeparadas { get; set; } = new();

    protected override async Task OnInitializedAsync()
    {
        cancha = CanchasService.Obtener(Id);
        if (cancha != null && !string.IsNullOrEmpty(cancha.descripcion))
            {
                var partes = cancha.descripcion.Split(new[] { "\n\n" }, StringSplitOptions.None);

                if (partes.Length > 1)
                {
                    DescripcionCorta = partes[0];

                    ReglasSeparadas = partes[1].Split('\n', StringSplitOptions.RemoveEmptyEntries)
                                               .Select(r => r.Trim())
                                               .ToList();
                }
                else
                {
                    DescripcionCorta = cancha.descripcion;
                }
            }

        var authState = await AuthStateProvider.GetAuthenticationStateAsync();
        var user = authState.User;
        esSoloLectura = user.Identity?.IsAuthenticated == true &&
                        (user.IsInRole("Propietario") || user.IsInRole("Admin"));
    }

    private void SeleccionarBloque(BloqueHorarioViewModel bloque)
    {
        if (bloque.estadoBloque != EstadoBloqueEnum.DISPONIBLE) return;
        if (bloquesSeleccionados.Contains(bloque))
            bloquesSeleccionados.Remove(bloque);
        else
            bloquesSeleccionados.Add(bloque);
    }

    private DiaSemanaEnum ConvertirADiaSemanaEnum(DayOfWeek day)
    {
        return day switch
        {
            DayOfWeek.Monday => DiaSemanaEnum.LUNES,
            DayOfWeek.Tuesday => DiaSemanaEnum.MARTES,
            DayOfWeek.Wednesday => DiaSemanaEnum.MIERCOLES,
            DayOfWeek.Thursday => DiaSemanaEnum.JUEVES,
            DayOfWeek.Friday => DiaSemanaEnum.VIERNES,
            DayOfWeek.Saturday => DiaSemanaEnum.SABADO,
            DayOfWeek.Sunday => DiaSemanaEnum.DOMINGO,
            _ => DiaSemanaEnum.NO_VALIDO
        };
    }

    private async Task IrAPagar()
    {
        if (!bloquesSeleccionados.Any() || cancha == null) return;

        try
        {
            var authState = await AuthStateProvider.GetAuthenticationStateAsync();
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
                NavigationManager.NavigateTo("/Login", forceLoad: true);
                return;
            }

            var reserva = new ReservaViewModel
            {
                estado = EstadoReservaEnum.PENDIENTE_PAGO,
                cliente = cliente,
                cancha = cancha,
                pago = null,
                bloques = bloquesSeleccionados.ToList()
            };

            ReservasService.Guardar(reserva, Estado.Nuevo);

            NavigationManager.NavigateTo($"/ReservaConfirmada/{reserva.idReserva}?fecha={fechaSeleccionada:yyyy-MM-dd}");
        }
        catch (Exception ex)
        {
            mensajeError = $"Error al procesar la reserva: {ex.Message}";
        }
    }

    public record ComentarioItem(string Nombre, string Texto);
}

public static class StringExtensions
{
    public static string ToLowerCapitalized(this string input)
    {
        if (string.IsNullOrEmpty(input)) return string.Empty;
        return char.ToUpper(input[0]) + input.Substring(1).ToLower();
    }
}