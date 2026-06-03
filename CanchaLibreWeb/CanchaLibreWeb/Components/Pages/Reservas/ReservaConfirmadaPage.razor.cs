using Microsoft.AspNetCore.Components;
namespace CanchaLibreWeb.Components.Pages.Reservas;

public partial class ReservaConfirmadaPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    // Parámetros que luego vendrán de query string o estado
    private string NombreCancha = "Cancha de Fútbol 5";
    private string UbicacionCancha = "Complejo Deportivo Central - Cancha #3";
    private string FechaFormateada = "Sábado, 15 de Junio 2026";
    private string HoraRango = "18:00 - 19:00";
    private string Duracion = "1 hora";
    private string MetodoPago = "Visa •••• 4242";
    private string CodigoReserva = "RES-2026-8547";

    public class ItemPago
    {
        public string Concepto { get; set; } = string.Empty;
        public double Monto { get; set; }
    }

    private List<ItemPago> itemsPago = new()
    {
        new() { Concepto = "Alquiler de cancha", Monto = 35.00 },
        new() { Concepto = "Iluminación",        Monto =  5.00 },
        new() { Concepto = "Servicio",           Monto =  2.00 },
    };

    private double Total => itemsPago.Sum(i => i.Monto);

    private void DescargarComprobante()
    {
        // Aquí luego llamas tu API para generar el PDF
    }

    private void VolverInicio() => NavigationManager.NavigateTo("/");

}