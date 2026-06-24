using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Canchas;

namespace CanchaLibreWeb.Components.Pages.CanchaView;

public partial class DetalleCanchaPage : ComponentBase
{
    [Parameter] public int Id { get; set; }
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private ICanchasServiceClient CanchasService { get; set; } = default!;

    private CanchaViewModel? cancha;
    private BloqueHorarioViewModel? bloqueSeleccionado;
    private DateTime fechaSeleccionada = DateTime.Today;

    // Propiedad calculada para capturar el cambio de fecha y forzar el refresco de horarios
    private DateTime FechaSeleccionada
    {
        get => fechaSeleccionada;
        set
        {
            if (fechaSeleccionada != value)
            {
                fechaSeleccionada = value;
                bloqueSeleccionado = null; // Reiniciar selección al cambiar de fecha
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
    private double PrecioSubtotal => bloqueSeleccionado?.precio ?? 0;
    private double CostoServicio => bloqueSeleccionado != null ? 5.0 : 0; // Comisión fija si selecciona horario
    private double PrecioTotal => PrecioSubtotal + CostoServicio;

    private List<ComentarioItem> comentarios = new()
    {
        new("Juan Sánchez", "La iluminación es excelente."),
        new("Ricardo Carrasco", "Muy bien cuidado el pasto."),
        new("Carlos García", "Muy puntuales con la entrega de chalecos.")
    };

    protected override void OnInitialized()
    {
        // Consumo directo de la API mediante tu ServiceRestClient sin datos mock
        cancha = CanchasService.Obtener(Id);
    }

    private void SeleccionarBloque(BloqueHorarioViewModel bloque)
    {
        bloqueSeleccionado = bloque;
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

    private void IrAPagar()
    {
        if (bloqueSeleccionado != null)
        {
            
            NavigationManager.NavigateTo("/ReservaConfirmada");
        }
    }

    public record ComentarioItem(string Nombre, string Texto);
}

// Método de extensión helper útil para mejorar las etiquetas de texto visuales
public static class StringExtensions
{
    public static string ToLowerCapitalized(this string input)
    {
        if (string.IsNullOrEmpty(input)) return string.Empty;
        return char.ToUpper(input[0]) + input.Substring(1).ToLower();
    }
}