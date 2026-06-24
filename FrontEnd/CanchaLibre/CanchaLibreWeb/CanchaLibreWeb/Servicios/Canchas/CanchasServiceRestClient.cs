using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Canchas;

public class CanchasServiceRestClient : BaseRestServiceClient<CanchaViewModel, CanchaRestDto>, ICanchasServiceClient
{
    public CanchasServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory)
    {
        // IConfiguration e IHttpClientFactory son inyectados por el contenedor de DI.
    }
    private const string ResourcePath = "canchas";


    public List<CanchaViewModel> Listar()
    {
        var payload = Api.Get<List<CanchaRestDto>>(ResourcePath);

        var response = new List<CanchaViewModel>(payload.Count);
        foreach (var item in payload)
        {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public CanchaViewModel? Obtener(int id)
    {
        try
        {
            var payload = Api.Get<CanchaRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload);
        }
        catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound)
        {
            return null;
        }
    }

    public void Guardar(CanchaViewModel modelo, Estado estado)
    {
        var payload = ToRest(modelo);
        switch (estado)
        {
            case Estado.Nuevo:
                Api.Post(ResourcePath, payload);
                break;
            case Estado.Modificado:
                Api.Put($"{ResourcePath}/{modelo.idCancha}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id)
    {
        Api.Delete($"{ResourcePath}/{id}");
    }

    protected override CanchaViewModel ToViewModel(CanchaRestDto source)
{
    return new CanchaViewModel
    {
        idCancha = source.idCancha,
        activo = source.activo,
        nombre = source.nombre,
        descripcion = source.descripcion,
        direccion = source.direccion,
        imagenUrl = source.imagenUrl,
        disponible = source.disponible,
        precioBase = source.precioBase, 
        promedioCalificacion = source.promedioCalificacion, 
        deportes = ParseEnumDeportes(source.deportes),
        bloques = ParseBloques(source.bloques),
        etiquetas = ParseEnumEtiquetas(source.etiquetas), 
        propietario = source.propietario != null ? new PropietarioViewModel {
            Id = source.propietario.Id,
            Nombres = source.propietario.Nombres,
            Correo = source.propietario.Correo,
            Telefono = source.propietario.Telefono
        } : null
    };
}
private List<EtiquetaEnum> ParseEnumEtiquetas(List<string>? etiquetas)
{
    if (etiquetas == null || !etiquetas.Any()) return new List<EtiquetaEnum>();
    var result = new List<EtiquetaEnum>();
    foreach (var etiqueta in etiquetas)
    {
        if (Enum.TryParse<EtiquetaEnum>(etiqueta, true, out var parsed))
        {
            result.Add(parsed);
        }
    }
    return result;
}

    private List<BloqueHorarioViewModel> ParseBloques(List<BloqueHorarioRestDto>? bloques)
    {
        if(bloques == null || !bloques.Any())  return new List<BloqueHorarioViewModel>();
        List<BloqueHorarioViewModel> bloquesView = new List<BloqueHorarioViewModel>();
        foreach(var bloque in bloques)
        {
            bloquesView.Add(
                new BloqueHorarioViewModel{
                    diaSemana = toDiaSemana(bloque.diaSemana),
                    estadoBloque = toEstadoBloque(bloque.estadoBloque),
                    horaFin = bloque.horaFin,
                    horaInicio = bloque.horaInicio,
                    precio = bloque.precio
                }
            );
        }
        return bloquesView;
    }

    private EstadoBloqueEnum toEstadoBloque(string estadoBloque)
    {
        if (Enum.TryParse<EstadoBloqueEnum>(estadoBloque, true, out EstadoBloqueEnum estadoConvertido))
        {
            return estadoConvertido;
        }
        else
        {
            return EstadoBloqueEnum.NO_VALIDO;
        }
    }

    private DiaSemanaEnum toDiaSemana(string diaSemana)
    {
        if (Enum.TryParse<DiaSemanaEnum>(diaSemana, true, out DiaSemanaEnum diaConvertido))
        {
            return diaConvertido;
        }
        else
        {
            return DiaSemanaEnum.NO_VALIDO;
        }
    }

    private List<DeporteEnum> ParseEnumDeportes(List<string>? deportes)
    {
        if(deportes == null || !deportes.Any()) return new List<DeporteEnum>();
        var result = new List<DeporteEnum>();
        foreach (var deporte in deportes)
        {
            if (Enum.TryParse<DeporteEnum>(deporte, true, out var parsed))
            {
                result.Add(parsed);
            }
        }
        return result;
    }

    protected override CanchaRestDto ToRest(CanchaViewModel source)
    {
        return new CanchaRestDto
        {
            idCancha = source.idCancha,
            nombre = source.nombre,
            descripcion = source.descripcion,
            direccion = source.direccion,
            imagenUrl = source.imagenUrl,
            disponible = source.disponible,
            deportes = ParseStringDeportes(source.deportes)
        };
    }

    private List<string> ParseStringDeportes(List<DeporteEnum>? deportes)
    {
        if (deportes == null)
        {
            return new List<string>();
        }
        var result = new List<string>();
        foreach (var deporte in deportes)
        {
            result.Add(deporte.ToString());
        }
        return result;
    }
}