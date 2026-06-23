using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Cuentas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using CanchaLibreWeb.ViewModels;
using System.Net;
using System.Net.Http.Json;

namespace CanchaLibreWeb.Servicios.Cuentas;

public class  CuentasUsuarioRestClient : BaseRestServiceClient<CuentaUsuarioViewModel, CuentaUsuarioRestDto>, 
    ICuentasUsuarioServiceClient 
{
    private const string ResourcePath = "cuentas";

    public CuentasUsuarioRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory)
    {
    }

    public bool? Login(string username, string password)
    {
        var payload = new CuentaUsuarioRestDto {
            UserName = username.Trim(),
            Password = password
        };

        try {
            Api.Post($"{ResourcePath}/login", payload);
            return true;
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.Unauthorized) {
            return false;
        }
    }

    public List<CuentaUsuarioViewModel> Listar()
    {
         var payload = Api.Get<List<CuentaUsuarioRestDto>>(ResourcePath);

        var respuesta = new List<CuentaUsuarioViewModel>();
        foreach (var item in payload) {
            respuesta.Add(ToViewModel(item, includePassword: false));
        }

        return respuesta;
    }

    public CuentaUsuarioViewModel? Obtener(int id)
    {
        try
        {
            var payload = Api.Get<CuentaUsuarioRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload, includePassword: true);
        }
        catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound)
        {
            return null;
        }
    }

    public CuentaUsuarioViewModel? ObtenerPorUsername(string username)
    {
        return Listar().FirstOrDefault(actual =>
            string.Equals(actual.UserName, username, StringComparison.OrdinalIgnoreCase));
    }

    public void Guardar(CuentaUsuarioViewModel modelo, Estado estado)
    {
        var fallback = string.Empty;

        if (modelo.Id > 0)
        {
            var actual = Obtener(modelo.Id);
            fallback = actual?.Password ?? string.Empty;
        }

        var payload = ToRest(modelo, fallback);

        switch (estado)
        {
            case Estado.Nuevo:
                Api.Post(ResourcePath, payload);
                break;

            case Estado.Modificado:
                Api.Put($"{ResourcePath}/{modelo.Id}", payload);
                break;

            case Estado.Eliminado:
                Api.Delete($"{ResourcePath}/{modelo.Id}");
                break;

            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id)
    {
        Api.Delete($"{ResourcePath}/{id}");
    }

    protected override CuentaUsuarioViewModel ToViewModel(CuentaUsuarioRestDto source)
    {
        return ToViewModel(source, includePassword: false);
    }

    protected override CuentaUsuarioRestDto ToRest(CuentaUsuarioViewModel source)
    {
        return ToRest(source, string.Empty);
    }

    private static CuentaUsuarioViewModel ToViewModel(CuentaUsuarioRestDto source, bool includePassword)
    {
          return new CuentaUsuarioViewModel {
            Id = source.Id,
            Activo = source.Activo,
            UserName = source.UserName,
            Password = includePassword ? source.Password : string.Empty,
        };
    }

    private static CuentaUsuarioRestDto ToRest(CuentaUsuarioViewModel source, string passwordFallback)
    {
        var password = string.IsNullOrWhiteSpace(source.Password)
            ? passwordFallback
            : source.Password;

        return new CuentaUsuarioRestDto
        {
            Id = source.Id,
            UserName = source.UserName.Trim(),
            Password = password
        };
    }

   
}