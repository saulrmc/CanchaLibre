using Microsoft.AspNetCore.Components;
using System;
using System.Collections.Generic;

namespace CanchaLibreWeb.Components.Pages.PortalPropietario;

public partial class PortalPropietarioPage : ComponentBase
{
    protected int BusquedasTotales { get; set; } = 64;
    protected double BusquedasPorcentaje { get; set; } = 18;

    protected decimal UtilidadNeta { get; set; } = 6000;
    protected double UtilidadPorcentaje { get; set; } = 5;

    protected int ReservasMes { get; set; } = 70;
    protected double ReservasPorcentaje { get; set; } = 32;

    protected decimal IngresosTotales { get; set; } = 20000;
    protected double IngresosPorcentaje { get; set; } = 18;

    protected int PorcentajeCompletado { get; set; } = 80;
    protected decimal MontoCobrado { get; set; } = 100;
    protected decimal MontoPendiente { get; set; } = 20;

    protected List<ReservaReciente> ReservasRecientes { get; set; } = new();

    protected override void OnInitialized()
    {
        CargarDatosDashboard();
    }

    private void CargarDatosDashboard()
    {
        ReservasRecientes = new List<ReservaReciente>
        {
            new ReservaReciente
            {
                ComplejoDeportivo = "Complejo deportivo Elite Box",
                Fecha = new DateTime(2026, 05, 20),
                HoraInicio = "11:00",
                HoraFin = "13:00",
                Estado = "Cobrado",
            },
            new ReservaReciente
            {
                ComplejoDeportivo = "VillaSport",
                Fecha = new DateTime(2026, 05, 15),
                HoraInicio = "11:00",
                HoraFin = "13:00",
                Estado = "Por cobrar",
            }
        };
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