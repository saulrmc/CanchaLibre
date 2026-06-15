using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.CanchaView;

public partial class EncontraCanchaPage : ComponentBase
{
    [Inject] private NavigationManager NavigationManager { get; set; } = default!;

    private string ordenSeleccionado = "precio_asc";
    private int precioMax = 1400;

    private List<string> deportesSeleccionados = new();
    private List<string> caracteristicasSeleccionadas = new();

    private List<string> deportesDisponibles = new() { "Fútbol", "Voley", "Basquet", "Tenis" };
    private List<string> caracteristicasDisponibles = new()
        { "Floodlights", "Changing Room", "Parking", "Drinking Water", "AC", "First Aid" };

    public class CanchaListaItem
    {
        public int Id { get; set; }
        public string Nombre { get; set; } = string.Empty;
        public string Distrito { get; set; } = string.Empty;
        public string ImagenUrl { get; set; } = string.Empty;
        public int PrecioHora { get; set; }
        public List<string> Etiquetas { get; set; } = new();
        public List<string> Deportes { get; set; } = new();
        public bool EsOferta { get; set; }
    }

    private List<CanchaListaItem> todasLasCanchas = new()
    {
        new() { Id=1, Nombre="Fútbol 010", Distrito="Municipalidad de Santiago d.", ImagenUrl="cancha2.jpg",
                PrecioHora=80,  Etiquetas=new(){"Iluminación"}, Deportes=new(){"Fútbol"} },
        new() { Id=2, Nombre="Cancha Voley 012", Distrito="Avenida Brasil 1231, Jesús ...", ImagenUrl="cancha3.jpg",
                PrecioHora=120, Etiquetas=new(){"Iluminación","Wifi"}, Deportes=new(){"Voley"} },
        new() { Id=3, Nombre="Complejo Deportivo Cir...", Distrito="SJL", ImagenUrl="cancha4.jpeg",
                PrecioHora=80,  Etiquetas=new(){"Iluminación","Parking"}, Deportes=new(){"Fútbol"} },
        new() { Id=4, Nombre="Cancha mixta", Distrito="SMP", ImagenUrl="cancha1.jpg",
                PrecioHora=90,  Etiquetas=new(){"Iluminación","Parking"}, Deportes=new(){"Basquet","Fútbol"} },
        new() { Id=5, Nombre="Cancha privada", Distrito="Lince", ImagenUrl="cancha2.jpg",
                PrecioHora=100, Etiquetas=new(){"Iluminación","Wifi"}, Deportes=new(){"Tenis"}, EsOferta=true },
        new() { Id=6, Nombre="Cancha Tenis", Distrito="Jose Larco 4210, Miraflores", ImagenUrl="cancha3.jpg",
                PrecioHora=85,  Etiquetas=new(){"Parking","Wifi"}, Deportes=new(){"Tenis"} },
    };

    private List<CanchaListaItem> canchasFiltradas = new();

    protected override void OnInitialized()
    {
        canchasFiltradas = new(todasLasCanchas);
    }

    private void ToggleDeporte(string deporte, ChangeEventArgs e)
    {
        if ((bool)e.Value!) deportesSeleccionados.Add(deporte);
        else deportesSeleccionados.Remove(deporte);
        AplicarFiltros();
    }

    private void ToggleCaracteristica(string caract, ChangeEventArgs e)
    {
        if ((bool)e.Value!) caracteristicasSeleccionadas.Add(caract);
        else caracteristicasSeleccionadas.Remove(caract);
        AplicarFiltros();
    }

    private void AplicarFiltros()
    {
        canchasFiltradas = todasLasCanchas
            .Where(c => c.PrecioHora <= precioMax)
            .Where(c => deportesSeleccionados.Count == 0 ||
                        c.Deportes.Any(d => deportesSeleccionados.Contains(d)))
            .Where(c => caracteristicasSeleccionadas.Count == 0 ||
                        caracteristicasSeleccionadas.All(x => c.Etiquetas.Contains(x)))
            .ToList();
        Ordenar();
    }

    private void Ordenar()
    {
        canchasFiltradas = ordenSeleccionado switch
        {
            "precio_desc" => canchasFiltradas.OrderByDescending(c => c.PrecioHora).ToList(),
            "nombre" => canchasFiltradas.OrderBy(c => c.Nombre).ToList(),
            _ => canchasFiltradas.OrderBy(c => c.PrecioHora).ToList()
        };
    }

    private void LimpiarFiltros()
    {
        deportesSeleccionados.Clear();
        caracteristicasSeleccionadas.Clear();
        precioMax = 1400;
        ordenSeleccionado = "precio_asc";
        canchasFiltradas = new(todasLasCanchas);
    }

    private void IrADetalle(int id) => NavigationManager.NavigateTo($"/cancha/{id}");

    private string ObtenerColorDeporte(string deporte) => deporte switch
    {
        "Fútbol" => "chip-futbol",
        "Voley" => "chip-voley",
        "Basquet" => "chip-basquet",
        "Tenis" => "chip-tenis",
        _ => ""
    };
}