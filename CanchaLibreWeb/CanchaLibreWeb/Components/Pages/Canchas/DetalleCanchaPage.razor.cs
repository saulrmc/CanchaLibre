using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.Canchas;

public partial class DetalleCanchaPage : ComponentBase
{
    [Parameter] public int Id { get; set; }

    private CanchaViewModel cancha = new();
    private DateTime fechaSeleccionada = DateTime.Today;
    private string horaSeleccionada = string.Empty;

    private List<HorarioItem> horarios = new()
    {
        new("06:00 - 07:00 AM", 95), new("07:00 - 08:00 AM", 95),
        new("08:00 - 09:00 AM", 80), new("09:00 - 10:00 AM", 80),
        new("01:00 - 03:00 PM", 80), new("03:00 - 04:00 PM", 50),
        new("04:00 - 05:00 PM", 100),new("05:00 - 06:00 PM", 100),
    };

    private List<ComentarioItem> comentarios = new()
    {
        new("Juan Sanchéz",  "Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Guru Mahinaar", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Property Ecosaka", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Guru Mahinaar", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Milestone Proper", "Comentario breve."),
        new("Property Makkhan","Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Ahaly Jankia", "Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
        new("Property Makkhan","Lorem ipsum dolor sit amet, consectetur adipiscing elit."),
    };

    protected override void OnInitialized()
    {
        // Simulación — luego reemplaza con llamada HTTP a tu API
        cancha = new CanchaViewModel
        {
            idCancha = Id,
            nombre = "Complejo Deportivo Ciro Alegría",
            descripcion = "Premium 5-a-side and 7-a-side football turf with international standard artificial grass. Evening floodlights available.",
            imagenUrl = "cancha4.jpeg",
            direccion = "San Juan de Lurigancho",
            disponible = true,
            deportes = new() { DeporteEnum.FUTBOL },
            etiquetas = new() { EtiquetaEnum.ILUMINACIÓN, EtiquetaEnum.PARKING }
        };
    }

    private void SeleccionarHora(HorarioItem h) => horaSeleccionada = h.Etiqueta;

    public record HorarioItem(string Etiqueta, int Precio);
    public record ComentarioItem(string Nombre, string Texto);
}
