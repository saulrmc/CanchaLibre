using CanchaLibreWeb.Servicios.Canchas;
using CanchaLibreWeb.ViewModels;
using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Components.Forms;
using System.Linq;
//using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Components.Pages.PublicarCancha;

public partial class PublicarCanchaPage : ComponentBase
{
    [Inject] public ICanchasServiceClient RestClient { get; set; } = default!;

    protected int PasoActual { get; set; } = 1;

    [SupplyParameterFromForm(FormName = "PublicarCanchaForm")]
    public CanchaViewModel Modelo { get; set; } = default!;
    protected EditContext FormContext { get; set; } = default!;
    private ValidationMessageStore mensajeStore = null!;
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
            activo = true
        };
        FormContext = new EditContext(Modelo);
        mensajeStore = new ValidationMessageStore(FormContext);

    }

    protected List<string> DeportesDisponibles = new() { "Fútbol", "Basquet", "Voley", "Tenis" };

    protected List<CaracteristicaItem> CaracteristicasDisponibles = new()
    {
        new() { Nombre = "ILUMINACIÓN", Icono = "💡" },
        new() { Nombre = "PARKING", Icono = "🚗" },
        new() { Nombre = "WIFI", Icono = "📶" },
        new() { Nombre = "VESTIDORES", Icono = "👕" },
        new() { Nombre = "DUCHAS", Icono = "🚿" },
        new() { Nombre = "BAÑOS", Icono = "🚽" }
    };

    protected async Task SiguientePaso()
    {
        if (PasoActual == 1)
        {
            //FormContext.Validate();

            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.nombre));
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.precioBase));
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.descripcion));

            bool tieneErroresPaso1 = FormContext.GetValidationMessages(() => Modelo.nombre).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.precioBase).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.descripcion).Any();

            if (tieneErroresPaso1) return;
        }
        else if (PasoActual == 2)
        {
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.distrito));
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.direccion));


            bool tieneErroresPaso2 = FormContext.GetValidationMessages(() => Modelo.distrito).Any() ||
                                    FormContext.GetValidationMessages(() => Modelo.direccion).Any();

            if (tieneErroresPaso2) return;
        }
        else if (PasoActual == 3)
        {
            mensajeStore.Clear(FieldIdentifier.Create(() => HorariosSeleccionados));
            if (HorariosSeleccionados == null || !HorariosSeleccionados.Any())
            {
                mensajeStore.Add(FieldIdentifier.Create(() => HorariosSeleccionados), "Debe seleccionar al menos un bloque horario para la disponibilidad.");
                FormContext.NotifyValidationStateChanged();
                return;
            }
        }
        else if (PasoActual == 4)
        {
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.imagenUrl));
            bool tieneErroresPaso4 = FormContext.GetValidationMessages(() => Modelo.imagenUrl).Any();
            if (tieneErroresPaso4) return;

            await FinalizarPublicacion();
            return;
        }

        if (PasoActual < 4)
        {
            if (PasoActual == 1)
            {
                mensajeStore.Clear();

                FormContext.MarkAsUnmodified(FieldIdentifier.Create(() => Modelo.distrito));
                FormContext.MarkAsUnmodified(FieldIdentifier.Create(() => Modelo.direccion));

                FormContext.MarkAsUnmodified(FieldIdentifier.Create(() => Modelo.imagenUrl));
                FormContext.NotifyValidationStateChanged();
            }
            PasoActual++;
            StateHasChanged();
        }
    }

    protected void RefrescarValidacionDistrito()
    {
        if (!string.IsNullOrEmpty(Modelo.distrito))
        {
            FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.distrito));
            StateHasChanged();
        }
    }


    protected string VistaPreviaImagenUrl { get; set; } = string.Empty;
    protected string MensajeImagen { get; set; } = string.Empty;
    private const long MaxImageSize = 5 * 1024 * 1024; // 5 MB

    protected async Task CargarImagen(InputFileChangeEventArgs e)
    {
        MensajeImagen = string.Empty;
        var archivo = e.File;

        if (archivo == null) return;

        if (archivo.Size > MaxImageSize)
        {
            MensajeImagen = "La imagen excede el límite permitido de 5 MB.";
            VistaPreviaImagenUrl = string.Empty;
            Modelo.imagenUrl = string.Empty; // Limpiamos el modelo si ya había una previa
            return;
        }

        var tipoArchivo = archivo.ContentType.ToLower();
        if (tipoArchivo != "image/jpeg" && tipoArchivo != "image/jpg" && tipoArchivo != "image/png")
        {
            MensajeImagen = "Formato no soportado. Solo se permiten imágenes .jpg, .jpeg o .png";
            VistaPreviaImagenUrl = string.Empty;
            Modelo.imagenUrl = string.Empty;
            return;
        }

        try
        {
            using var stream = archivo.OpenReadStream(MaxImageSize);
            using var memoryStream = new MemoryStream();
            await stream.CopyToAsync(memoryStream);
            var bytes = memoryStream.ToArray();

            string base64String = Convert.ToBase64String(bytes);
            VistaPreviaImagenUrl = $"data:{archivo.ContentType};base64,{base64String}";

            Modelo.imagenUrl = VistaPreviaImagenUrl;

            mensajeStore.Clear(FieldIdentifier.Create(() => Modelo.imagenUrl));
        }
        catch (Exception)
        {
            MensajeImagen = "Ocurrió un error inesperado al procesar la imagen. Inténtalo de nuevo.";
            VistaPreviaImagenUrl = string.Empty;
            Modelo.imagenUrl = string.Empty;
        }
        finally
        {
            StateHasChanged();
        }
    }
    protected async Task ProcesarPublicacionFinal()
    {
        //FormContext.Validate();

        FormContext.NotifyFieldChanged(FieldIdentifier.Create(() => Modelo.imagenUrl));

        bool tieneErroresPaso4 = FormContext.GetValidationMessages(() => Modelo.imagenUrl).Any();
        if (tieneErroresPaso4) return;

        await FinalizarPublicacion();
    }

    protected void PasoAnterior()
    {
        if (PasoActual > 1)
        {
            mensajeStore.Clear(FieldIdentifier.Create(() => HorariosSeleccionados));
            PasoActual--;
            StateHasChanged();
        }
    }

    protected List<BloqueHorarioViewModel> HorariosSeleccionados { get; set; } = new();
    private BloqueHorarioViewModel? ObtenerBloque(DiaSemanaEnum dia, TimeOnly hora)
    {
        return HorariosSeleccionados.FirstOrDefault(b => b.diaSemana == dia && b.horaInicio == hora);
    }

    private bool mostrarModalPrecio = false;
    private BloqueHorarioViewModel? bloqueAEditar;
    private double nuevoPrecioInput;

    private void ToggleSlot(DiaSemanaEnum dia, TimeOnly hora)
    {
        var bloque = ObtenerBloque(dia, hora);

        if (bloque != null)
        {
            bloqueAEditar = bloque;
            nuevoPrecioInput = bloque.precio;
            mostrarModalPrecio = true;
        }
        else
        {
            double precioActual = Modelo.precioBase;
            HorariosSeleccionados.Add(new BloqueHorarioViewModel
            {
                diaSemana = dia,
                horaInicio = hora,
                horaFin = hora.AddHours(1),
                precio = precioActual,
                estadoBloque = EstadoBloqueEnum.DISPONIBLE
            });

            mensajeStore.Clear(FieldIdentifier.Create(() => HorariosSeleccionados));
        }

        StateHasChanged();
    }

    private void GuardarPrecioIndividual()
    {
        if(nuevoPrecioInput <= 0){nuevoPrecioInput=Modelo.precioBase;}
        if (bloqueAEditar != null)
        {
            bloqueAEditar.precio = nuevoPrecioInput;
        }
        CerrarModal();
    }

    private void EliminarBloqueIndividual()
    {
        if (bloqueAEditar != null)
        {
            HorariosSeleccionados.Remove(bloqueAEditar);
        }
        CerrarModal();
    }

    private void CerrarModal()
    {
        mostrarModalPrecio = false;
        bloqueAEditar = null;
        StateHasChanged();
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

            string deporteLimpio = DeporteSeleccionado;
            if (!string.IsNullOrEmpty(deporteLimpio))
            {
                deporteLimpio = deporteLimpio.ToUpper() switch
                {
                    "FÚTBOL" => "FUTBOL",
                    _ => deporteLimpio.ToUpper()
                };
            }

            if (Enum.TryParse<DeporteEnum>(deporteLimpio, true, out var deporteEnum))
            {
                Modelo.deportes = new List<DeporteEnum> { deporteEnum };
            }

            Modelo.etiquetas = new List<EtiquetaEnum>();

            foreach (var caracteristica in CaracteristicasCopia)
            {

                string caracteristicaLimpia = caracteristica.ToUpper() switch
                {
                    "ILUMINACIÓN" => "ILUMINACION",
                    "BAÑOS" => "BANOS",
                    _ => caracteristica.ToUpper() // PARKING, WIFI, VESTIDORES, DUCHAS
                };

                if (Enum.TryParse<EtiquetaEnum>(caracteristicaLimpia, true, out var etiquetaEnum))
                {
                    Modelo.etiquetas.Add(etiquetaEnum);
                }
            }

            //Modelo.bloques = new List<BloqueHorarioViewModel>();
            Modelo.bloques = HorariosSeleccionados;



            var authState = await AuthStateProvider.GetAuthenticationStateAsync();
            var usuario = authState.User;
            var idPropietarioClaim = usuario.FindFirst("IdUsuario")?.Value;

            if (string.IsNullOrEmpty(idPropietarioClaim))
            {
                throw new NullReferenceException("El Claim 'IdUsuario' no existe en la sesión actual del ClaimsPrincipal.");
            }

            if (!int.TryParse(idPropietarioClaim, out var idPropietario))
            {
                throw new FormatException($"El valor del Claim 'IdUsuario' ('{idPropietarioClaim}') no tiene un formato numérico entero (int) válido.");
            }

            var propietarioReal = PropietariosClient.Obtener(idPropietario);
            if (propietarioReal == null)
            {
                throw new KeyNotFoundException($"El Propietario con ID {idPropietario} se leyó de la sesión pero no existe en la base de datos de Java (GlassFish devolvió 404/Null).");
            }

            Modelo.propietario = propietarioReal;
            if (string.IsNullOrEmpty(Modelo.distrito)) Modelo.distrito = "Por definir";

            if (!string.IsNullOrWhiteSpace(ReglasInternas))
            {
                Modelo.descripcion = $"{Modelo.descripcion}\n\n{ReglasInternas}";
            }

            RestClient.Guardar(Modelo, Estado.Nuevo);

            await Task.Delay(500);
            PasoActual = 5;
        }
        catch (Exception ex)
        {
        //    MensajeError = "Hubo un problema al registrar la cancha en el servidor. Por favor, inténtalo de nuevo.";
            string mensajeCompleto = $"{ex.Message} -> {ex.InnerException?.Message}";

            // Si el error viene empaquetado en el HTML pesado de GlassFish, extraemos el pre
            var match = System.Text.RegularExpressions.Regex.Match(mensajeCompleto, @"<pre>(.*?)</pre>", System.Text.RegularExpressions.RegexOptions.IgnoreCase);

            if (match.Success)
            {
                // Muestra la traza exacta de Java (ej: NullPointerException, SQLException, etc.)
                MensajeError = $"Falla en Java: {match.Groups[1].Value.Trim()}";
            }
            else
            {
                // Muestra el error técnico real de C# (ej: El nombre del tipo de excepción y su mensaje interno)
                MensajeError = $"Falla en C#: [{ex.GetType().Name}] {ex.Message}";
            }
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