using CanchaLibreWeb.Servicios.Notificaciones;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class DashboardAdminPage : ComponentBase
{
    
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;
    [Inject] private NotificacionService Notificaciones { get; set; } = default!;//
    protected class RegistroHorario
    {
        public string Hora { get; set; } = string.Empty;
        public int Porcentaje { get; set; } // Representará la altura en píxeles (máx 200-250)
    }

    protected List<RegistroHorario> DatosGrafico { get; set; } = new();

    private bool mostrarToastReserva = false;
    private string mensajeToastReserva = string.Empty;

    protected override void OnInitialized()
    {
        
        DatosGrafico = new List<RegistroHorario>
        {
            new() { Hora = "08:00", Porcentaje = 25 },
            new() { Hora = "09:00", Porcentaje = 40 },
            new() { Hora = "10:00", Porcentaje = 75 },
            new() { Hora = "11:00", Porcentaje = 95 },
            new() { Hora = "12:00", Porcentaje = 50 },
            new() { Hora = "13:00", Porcentaje = 70 },
            new() { Hora = "14:00", Porcentaje = 140 },
            new() { Hora = "15:00", Porcentaje = 165 },
            new() { Hora = "16:00", Porcentaje = 165 },
            new() { Hora = "17:00", Porcentaje = 140 },
            new() { Hora = "18:00", Porcentaje = 100 }
        };
        Notificaciones.ReservaConfirmada += OnReservaConfirmada;
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