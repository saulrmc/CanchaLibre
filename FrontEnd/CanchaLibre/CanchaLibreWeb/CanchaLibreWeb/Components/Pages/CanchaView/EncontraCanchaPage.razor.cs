using Microsoft.AspNetCore.Components;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Canchas;

namespace CanchaLibreWeb.Components.Pages.CanchaView;

public partial class EncontraCanchaPage : ComponentBase
{
    [Inject] private ICanchasServiceClient CanchasService { get; set; } = default!;
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    private string ordenSeleccionado = "precio_asc";
    private int precioMax = 300; // Ajustado a un rango de precio real por hora

    // Listas de selección basadas estrictamente en tus tipos Enum
    private List<DeporteEnum> deportesSeleccionados = new();
    private List<EtiquetaEnum> caracteristicasSeleccionadas = new();

    // Extracción automática de valores definidos en tus Enums (Cero hardcodeo)
    private List<DeporteEnum> deportesDisponibles = Enum.GetValues<DeporteEnum>().ToList();
    private List<EtiquetaEnum> caracteristicasDisponibles = Enum.GetValues<EtiquetaEnum>().ToList();

    private List<CanchaViewModel> todasLasCanchas = new();
    private List<CanchaViewModel>? canchasFiltradas;

    protected override async Task OnInitializedAsync()
    {
        todasLasCanchas = await Task.Run(() => CanchasService.Listar()) ?? new List<CanchaViewModel>();
        AplicarFiltros();
    }

    private void ToggleDeporte(DeporteEnum deporte, ChangeEventArgs e)
    {
        if ((bool)e.Value!) 
            deportesSeleccionados.Add(deporte);
        else 
            deportesSeleccionados.Remove(deporte);
            
        AplicarFiltros();
    }

    private void ToggleCaracteristica(EtiquetaEnum caract, ChangeEventArgs e)
    {
        if ((bool)e.Value!) 
            caracteristicasSeleccionadas.Add(caract);
        else 
            caracteristicasSeleccionadas.Remove(caract);
            
        AplicarFiltros();
    }

    private void AplicarFiltros()
    {
        canchasFiltradas = todasLasCanchas
            .Where(c => c.activo) // Seguridad elemental: no mostrar canchas eliminadas/ocultas
            .Where(c => c.precioBase <= precioMax)
            .Where(c => !deportesSeleccionados.Any() || 
                        (c.deportes != null && c.deportes.Any(d => deportesSeleccionados.Contains(d))))
            .Where(c => !caracteristicasSeleccionadas.Any() || 
                        (c.etiquetas != null && caracteristicasSeleccionadas.All(x => c.etiquetas.Contains(x))))
            .ToList();
            
        Ordenar();
    }

    private void Ordenar()
    {
        if (canchasFiltradas == null) return;

        canchasFiltradas = ordenSeleccionado switch
        {
            "precio_desc" => canchasFiltradas.OrderByDescending(c => c.precioBase).ToList(),
            "nombre" => canchasFiltradas.OrderBy(c => c.nombre, StringComparer.OrdinalIgnoreCase).ToList(),
            _ => canchasFiltradas.OrderBy(c => c.precioBase).ToList()
        };
    }

    private void LimpiarFiltros()
    {
        deportesSeleccionados.Clear();
        caracteristicasSeleccionadas.Clear();
        precioMax = 300;
        ordenSeleccionado = "precio_asc";
        AplicarFiltros();
    }

    // Helper visual para asignar estilos CSS dinámicos de acuerdo al deporte mapeado
    private string ObtenerColorDeporte(DeporteEnum deporte) => deporte switch
    {
        DeporteEnum.FUTBOL => "chip-futbol",
        DeporteEnum.VOLEY => "chip-voley",
        DeporteEnum.BASQUET => "chip-basquet",
        DeporteEnum.TENIS => "chip-tenis",
        _ => "chip-generico"
    };

    // Helper estético: Pasa de "ILUMINACIÓN_NOCTURNA" a "Iluminación nocturna"
    private string FormatearEnumTexto(string textoEnum)
    {
        if (string.IsNullOrEmpty(textoEnum)) return string.Empty;
        
        string limpio = textoEnum.Replace("_", " ").ToLower();
        return char.ToUpper(limpio[0]) + limpio.Substring(1);
    }
}