using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class DashboardAdminPage : ComponentBase
{
    // Estructura básica para modelar cada barra del gráfico
    protected class RegistroHorario
    {
        public string Hora { get; set; } = string.Empty;
        public int Porcentaje { get; set; } // Representará la altura en píxeles (máx 200-250)
    }

    protected List<RegistroHorario> DatosGrafico { get; set; } = new();

    protected override void OnInitialized()
    {
        // Cargamos los datos simulando los valores de la imagen de tu mock
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
    }
}