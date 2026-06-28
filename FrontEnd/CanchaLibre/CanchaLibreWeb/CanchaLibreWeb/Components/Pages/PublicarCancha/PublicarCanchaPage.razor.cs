using System.ComponentModel.DataAnnotations;
using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.ViewModels;
using CanchaLibreWeb.Servicios.Base;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Forms;
using System.Linq;

namespace CanchaLibreWeb.Components.Pages.PublicarCancha;

public partial class PublicarCanchaPage : ComponentBase
{
    [Inject] public ICanchasServiceClient RestClient { get; set; } = default!;

    protected int PasoActual { get; set; } = 1;

    [SupplyParameterFromForm(FormName = "PublicarCanchaForm")]
    public CanchaViewModel Modelo { get; set; } = default!;
    protected EditContext FormContext { get; set; } = default!;
    protected string DeporteSeleccionado { get; set; } = "Fútbol";
    protected string ReglasInternas { get; set; } = "Llega 10 minutos antes de la hora de la reserva.";
    protected List<string> CaracteristicasCopia { get; set; } = new();

    protected override void OnInitialized()
    {
        Modelo ??= new CanchaViewModel
        {
            nombre = string.Empty,
            descripcion = string.Empty,
            direccion = string.Empty,
            distrito = string.Empty,
            deportes = new List<DeporteEnum>(),
            etiquetas = new List<EtiquetaEnum>(),
            bloques = new List<BloqueHorarioViewModel>(),
            disponible = true,
            activo = true
        };
        FormContext = new EditContext(Modelo);
    }

    protected List<string> DeportesDisponibles = new() { "Fútbol", "Voley", "Basquet", "Tenis" };

    protected List<CaracteristicaItem> CaracteristicasDisponibles = new()
    {
        new() { Nombre = "ILUMINACION", Icono = "💡" },
        new() { Nombre = "PARKING", Icono = "🚗" },
        new() { Nombre = "WIFI", Icono = "📶" },
        new() { Nombre = "VESTIDORES", Icono = "👕" },
        new() { Nombre = "DUCHAS", Icono = "🚿" },
        new() { Nombre = "BAÑOS", Icono = "🚽" }
    };

    protected void SiguientePaso()
    {
        FormContext.Validate();

        if (PasoActual == 1)
        {
            bool tieneErroresPaso1 = FormContext.GetValidationMessages(() => Modelo.nombre).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.precioBase).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.descripcion).Any();

            if (tieneErroresPaso1) return;
        }
        else if (PasoActual == 2)
        {
            bool tieneErroresPaso2 = FormContext.GetValidationMessages(() => Modelo.distrito).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.direccion).Any();

            if (tieneErroresPaso2) return;
        }

        if (PasoActual < 3)
        {
            PasoActual++;
            StateHasChanged();
        }
    }

    protected void PasoAnterior()
    {
        if (PasoActual > 1)
        {
            PasoActual--;
            StateHasChanged();
        }
    }

    protected bool IsLoading { get; set; } = false;
    protected string? MensajeError { get; set; }

    protected async Task FinalizarPublicacion()
    {
        try
        {
            IsLoading = true;
            MensajeError = null;
            StateHasChanged();

            // Sincronizamos las variables del formulario con los Enums del Backend en Java
            if (Enum.TryParse<DeporteEnum>(DeporteSeleccionado, true, out var deporteEnum))
            {
                Modelo.deportes = new List<DeporteEnum> { deporteEnum };
            }

            Modelo.etiquetas = new List<EtiquetaEnum>();
            foreach (var caracteristica in CaracteristicasCopia)
            {
                if (Enum.TryParse<EtiquetaEnum>(caracteristica, true, out var etiquetaEnum))
                {
                    Modelo.etiquetas.Add(etiquetaEnum);
                }
            }

            // Ejecutamos la inserción en el hilo de fondo
            await Task.Run(() =>
            {
                RestClient.Guardar(Modelo, Estado.Nuevo);
            });

            await Task.Delay(500); // Pequeño respiro táctico visual
            PasoActual = 4;
        }
        catch (Exception)
        {
            MensajeError = "Hubo un problema al registrar la cancha en el servidor. Por favor, inténtalo de nuevo.";
        }
        finally
        {
            IsLoading = false;
            StateHasChanged();
        }
    }

    public class CaracteristicaItem
    {
        public string Nombre { get; set; } = string.Empty;
        public string Icono { get; set; } = string.Empty;
        public bool Seleccionado { get; set; }
    }
}