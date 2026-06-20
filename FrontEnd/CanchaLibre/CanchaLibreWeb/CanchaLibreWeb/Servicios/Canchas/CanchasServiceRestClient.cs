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

    public List<CanchaViewModel> Listar()
    {
        var payload = Api.Get<List<CanchaRestDto>>("api/v1/canchas");

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
            var payload = Api.Get<CanchaRestDto>($"api/v1/canchas/{id}");
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
                Api.Post("api/v1/canchas", payload);
                break;
            case Estado.Modificado:
                Api.Put($"api/v1/canchas/{modelo.idCancha}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id)
    {
        Api.Delete($"api/v1/canchas/{id}");
    }

    protected override CanchaViewModel ToViewModel(CanchaRestDto source)
    {
        return new CanchaViewModel
        {
            idCancha = source.idCancha,
            nombre = source.nombre,
            descripcion = source.descripcion,
            direccion = source.direccion,
            imagenUrl = source.imagenUrl,
            disponible = source.disponible,
            deportes = ParseEnumDeportes(source.deportes),
        };
    }

    private List<DeporteEnum> ParseEnumDeportes(List<string> deportes)
    {
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