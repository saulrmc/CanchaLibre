using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.MisReservas;

public partial class MisReservasPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    public class ReservaUsuarioItem
    {
        public string BookingId { get; set; } = string.Empty;
        public string TransactionId { get; set; } = string.Empty;
        public string CanchaNombre { get; set; } = string.Empty;
        public string Distrito { get; set; } = string.Empty;
        public string ImagenUrl { get; set; } = string.Empty;
        public DateTime Fecha { get; set; }
        public string Horario { get; set; } = string.Empty;
        public int PrecioTotal { get; set; }
    }

    // SOLUCIÓN: Cambiado a 'protected' para que el archivo .razor pueda leerlo sin problemas
    protected List<ReservaUsuarioItem> reservasUsuario { get; set; } = new();

    protected override void OnInitialized()
    {
        reservasUsuario = new List<ReservaUsuarioItem>
        {
            new()
            {
                BookingId = "BK002",
                TransactionId = "TXN1234567891",
                CanchaNombre = "Complejo Deportivo Elite Box",
                Distrito = "Surco",
                ImagenUrl = "cancha2.jpg",
                Fecha = new DateTime(2026, 04, 13),
                Horario = "11:00 - 13:00",
                PrecioTotal = 100
            },
            new()
            {
                BookingId = "BK001",
                TransactionId = "TXN9876543219",
                CanchaNombre = "Campo de Marte",
                Distrito = "Jesús María",
                ImagenUrl = "cancha1.jpg",
                Fecha = new DateTime(2026, 05, 20),
                Horario = "11:00 - 13:00",
                PrecioTotal = 80
            }
        };
    }

    protected void ModificarReserva(string bookingId)
    {
        Console.WriteLine($"Modificando reserva: {bookingId}");
    }

    protected void CancelarReserva(string bookingId)
    {
        var reserva = reservasUsuario.FirstOrDefault(r => r.BookingId == bookingId);
        if (reserva != null)
        {
            reservasUsuario.Remove(reserva);
            StateHasChanged();
        }
    }
}