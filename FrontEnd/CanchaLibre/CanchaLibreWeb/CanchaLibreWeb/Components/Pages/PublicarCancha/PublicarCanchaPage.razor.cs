using Microsoft.AspNetCore.Components;
using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.Components.Pages.PublicarCancha;

public partial class PublicarCanchaPage : ComponentBase
{
    protected int PasoActual { get; set; } = 1;
    protected CanchaPublishModel Modelo { get; set; } = new();

    // Listas de opciones basadas exactamente en la interfaz de filtrado y visualización
    protected List<string> DeportesDisponibles = new() { "Fútbol", "Voley", "Basquet", "Tenis" };
    
    protected List<CaracteristicaItem> CaracteristicasDisponibles = new()
    {
        new() { Nombre = "Floodlights", Icono = "💡" },
        new() { Nombre = "Changing Room", Icono = "👕" },
        new() { Nombre = "Parking", Icono = "🚗" },
        new() { Nombre = "Drinking Water", Icono = "💧" },
        new() { Nombre = "AC", Icono = "❄️" },
        new() { Nombre = "First Aid", Icono = "📦" }
    };

    protected void SiguientePaso()
    {
        if (PasoActual < 3) PasoActual++;
    }

    protected void PasoAnterior()
    {
        if (PasoActual > 1) PasoActual--;
    }

    protected void FinalizarPublicacion()
    {
        // Aquí se conectaría con tu API gateway / Backend en Java
        // await CanchaService.CrearAsync(Modelo);
        
        // Redirección simulada tras el éxito
        PasoActual = 4; 
    }

    // Modelos de datos y validación
    protected class CanchaPublishModel
    {
        [Required(ErrorMessage = "El nombre del complejo es obligatorio.")]
        public string Nombre { get; set; } = string.Empty;

        [Required(ErrorMessage = "La descripción ayuda a conseguir más reservas.")]
        public string Descripcion { get; set; } = string.Empty;

        [Required(ErrorMessage = "Define un precio por hora.")]
        public decimal PrecioPorHora { get; set; }

        [Required(ErrorMessage = "Debes seleccionar un deporte principal.")]
        public string DeportePrincipal { get; set; } = "Fútbol";

        [Required(ErrorMessage = "La dirección exacta es requerida.")]
        public string Direccion { get; set; } = string.Empty;

        [Required(ErrorMessage = "Selecciona el distrito.")]
        public string Distrito { get; set; } = string.Empty;

        public List<string> CaracteristicasSeleccionadas { get; set; } = new();
        public string Reglas { get; set; } = "Arrive 10 minutes before your booking time.";
    }

    protected class CaracteristicaItem
    {
        public string Nombre { get; set; } = string.Empty;
        public string Icono { get; set; } = string.Empty;
        public bool Seleccionado { get; set; }
    }
}