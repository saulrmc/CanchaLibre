using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;

namespace CanchaLibreWeb.Components.Pages.CanchaView;

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
        new("Juan Sanchéz",  "La iluminacion es buena"),
        new("Ricardo Carrasco", "Podria mejorar con la atencion."),
        new("Carlos Garcia", "Muy bien, son puntuales."),
        new("Luis Rodriguez", "Tuve que esperar 10 minutos extras...."),
        new("Rubio Huaman", "Comentario breve."),
        new("Camila Flores","Muy buena cancha"),
        new("Ahaly Jankia", "Muy buen servicio."),
        new("Luciana Ruiz","Deberia haber una maquina de snacks."),
    };

    protected override void OnInitialized()
    {
        // Simulación — luego reemplaza con llamada HTTP a tu API
        cancha = new CanchaViewModel
        {
            idCancha = Id,
            nombre = "Complejo Deportivo Ciro Alegría",
            descripcion = "En la Cancha Deportivo Ciro Alegría, situada en San Juan de Lurigancho, disfrutarás de un espacio deportivo renovado y de gran tamaño, ideal para partidos de fútbol 7 en un entorno vigilado y seguro.",
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
