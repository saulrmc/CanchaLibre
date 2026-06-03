using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Admin;

public partial class ReportesAdminPage : ComponentBase
{
    // Estructura para las filas del reporte de sedes
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

    // Estado de los filtros vinculados a la interfaz
    protected string TipoReporteActivo { get; set; } = "Comparativo"; // Pestaña seleccionada
    protected string PeriodoSeleccionado { get; set; } = "Último mes";
    protected string FiltrarPorSeleccionado { get; set; } = "Mayores reservas";

    protected override void OnInitialized()
    {
        // Rellenamos el maestro con la data exacta de tu captura
        _reportesMaster = new List<ReporteSedeRow>
        {
            new() { Id = 1, Sede = "Breña", Reservas = 512, Ingresos = 20000, HorasOcupacion = 160 },
            new() { Id = 2, Sede = "San Borja", Reservas = 256, Ingresos = 15000, HorasOcupacion = 100 },
            new() { Id = 3, Sede = "Miraflores", Reservas = 255, Ingresos = 12000, HorasOcupacion = 80 },
            new() { Id = 4, Sede = "Jesús María", Reservas = 128, Ingresos = 12000, HorasOcupacion = 90 },
            new() { Id = 5, Sede = "San Juan de Lurigancho", Reservas = 127, Ingresos = 10000, HorasOcupacion = 120 },
            new() { Id = 6, Sede = "La Victoria", Reservas = 126, Ingresos = 10000, HorasOcupacion = 110 }
        };

        ProcesarFiltrosYOrden();
    }

    // Cambiar de pestaña de reporte de forma interactiva
    protected void CambiarTipoReporte(string tipo)
    {
        TipoReporteActivo = tipo;
        // Opcional: Aquí podrías cambiar drásticamente la data según el tipo, 
        // pero por ahora mantendremos la consistencia visual
    }

    // Ejecutado al hacer clic en el botón "Buscar"
    protected void ProcesarFiltrosYOrden()
    {
        var consulta = _reportesMaster.AsEnumerable();

        // Aplicamos el orden seleccionado en el menú desplegable
        if (FiltrarPorSeleccionado == "Mayores reservas")
        {
            consulta = consulta.OrderByDescending(r => r.Reservas);
        }
        else if (FiltrarPorSeleccionado == "Mayores ingresos")
        {
            consulta = consulta.OrderByDescending(r => r.Ingresos);
        }
        else if (FiltrarPorSeleccionado == "Más horas ocupadas")
        {
            consulta = consulta.OrderByDescending(r => r.HorasOcupacion);
        }

        ReportesFiltrados = consulta.ToList();
    }
}