using CanchaLibreWeb.Servicios.Notificaciones;
using Microsoft.AspNetCore.Components;
namespace CanchaLibreWeb.Components.Pages.Reservas;

public partial class ReservaConfirmadaPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private NotificacionService Notificaciones { get; set; } = default!;//aun


    // Parámetros que luego vendrán de query string o estado
    private string NombreCancha = "Cancha de Fútbol 5";
    private string UbicacionCancha = "Complejo Deportivo Central - Cancha #3";
    private string FechaFormateada = "Sábado, 15 de Junio 2026";
    private string HoraRango = "18:00 - 19:00";
    private string Duracion = "1 hora";
    private string MetodoPago = "Yape •••• 4242";
    private string CodigoReserva = "RES-2026-8547";

    private bool mostrarToast = false;
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
    protected override async Task OnInitializedAsync()
    {
        // Aviso al cliente (esta misma página, no bloqueante, 10s)
        mostrarToast = true;
        _ = OcultarToastTrasRetraso();

        // Aviso al admin (cruza al circuito del dashboard, si está abierto)
        Notificaciones.NotificarReservaExitosa(new ReservaExitosaInfo
        {
            NombreCancha = NombreCancha,
            NombreCliente = "Cliente", // reemplazar cuando se tenga el nombre real del usuario logueado
        });
    }

    private async Task OcultarToastTrasRetraso()
    {
        await Task.Delay(10_000);
        mostrarToast = false;
        await InvokeAsync(StateHasChanged);
    }
    private void DescargarComprobante()
    {
        // Aquí luego llamas tu API para generar el PDF
    }

    private void VolverInicio() => NavigationManager.NavigateTo("/");

}