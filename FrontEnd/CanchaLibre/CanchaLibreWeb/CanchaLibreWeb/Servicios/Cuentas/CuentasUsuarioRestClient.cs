using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Cuentas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;
using System.Net;
using System.Net.Http.Json;

namespace CanchaLibreWeb.Servicios.Cuentas;

public class CuentasUsuarioRestClient
    : BaseRestServiceClient<UsuarioViewModel, CuentasUsuarioRestClient.CuentaUsuarioRestDto>,
      ICuentasUsuarioServiceClient
{
    private const string ResourcePath = "api/v1/cuentas";

    public CuentasUsuarioRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory)
    {
    }

    public UsuarioRespuestaRestDto? Login(string username, string password)
    {
        try
        {
            return Api.Post<CuentaUsuarioRestDto, UsuarioRespuestaRestDto>(
                $"{ResourcePath}/login",
                new CuentaUsuarioRestDto
                {
                    UserName = username.Trim(),
                    Password = password
                });
        }
        catch (HttpRequestException ex) when (
            ex.StatusCode == HttpStatusCode.Unauthorized ||
            ex.StatusCode == HttpStatusCode.NotFound ||
            ex.StatusCode == HttpStatusCode.BadRequest ||
            ex.StatusCode == HttpStatusCode.InternalServerError)
        {
            return null;
        }
        {
            return null;
        }
    }

    public List<UsuarioViewModel> Listar()
    {
        var payload = Api.Get<List<CuentaUsuarioRestDto>>(ResourcePath);
        return payload.Select(item => ToViewModel(item, includePassword: false)).ToList();
    }

    public UsuarioViewModel? Obtener(int id)
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

    public UsuarioViewModel? ObtenerPorUsername(string username)
    {
        return Listar().FirstOrDefault(actual =>
            string.Equals(actual.Nombres, username, StringComparison.OrdinalIgnoreCase));
    }

    public void Guardar(UsuarioViewModel modelo, Estado estado)
    {
        var fallback = string.Empty;

        if (modelo.Id > 0)
        {
            var actual = Obtener(modelo.Id);
            fallback = actual?.Contrasena ?? string.Empty;
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

    protected override UsuarioViewModel ToViewModel(CuentaUsuarioRestDto source)
    {
        return ToViewModel(source, includePassword: false);
    }

    protected override CuentaUsuarioRestDto ToRest(UsuarioViewModel source)
    {
        return ToRest(source, string.Empty);
    }

    private static UsuarioViewModel ToViewModel(CuentaUsuarioRestDto source, bool includePassword)
    {
        return new UsuarioViewModel
        {
            Id = source.Id,
            Nombres = source.UserName ?? string.Empty,
            Contrasena = includePassword ? source.Password ?? string.Empty : string.Empty
        };
    }

    private static CuentaUsuarioRestDto ToRest(UsuarioViewModel source, string passwordFallback)
    {
        var password = string.IsNullOrWhiteSpace(source.Contrasena)
            ? passwordFallback
            : source.Contrasena;

        return new CuentaUsuarioRestDto
        {
            Id = source.Id,
            UserName = source.Nombres.Trim(),
            Password = password
        };
    }

    public sealed class CuentaUsuarioRestDto
    {
        public int Id { get; set; }
        public string? UserName { get; set; }
        public string? Password { get; set; }
    }
}