using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public class ClientesServiceRestClient : BaseRestServiceClient<ClienteViewModel, ClienteRestDto>, IClientesServiceClient {
    public ClientesServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) {
        // IConfiguration e IHttpClientFactory son inyectados por el contenedor de DI.
    }
    private const string ResourcePath = "clientes";
    public List<ClienteViewModel> Listar() {
        var payload = Api.Get<List<ClienteRestDto>>($"{ResourcePath}");

        var response = new List<ClienteViewModel>(payload.Count);
        foreach (var item in payload) {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public ClienteViewModel? Obtener(int id) {
        try {
            var payload = Api.Get<ClienteRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }
    public ClienteViewModel? BuscarPorNombre(string nombre) {
        try {
            var path = $"{ResourcePath}/nombre/{Uri.EscapeDataString(nombre)}";
            var payload = Api.Get<ClienteRestDto>(path);
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }

    public void Guardar(ClienteViewModel modelo, Estado estado) {
        var payload = ToRest(modelo);
        switch (estado) {
            case Estado.Nuevo:
                Api.Post($"{ResourcePath}", payload);
                break;
            case Estado.Modificado:
                Api.Put($"{ResourcePath}/{modelo.Id}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id) {
        Api.Delete($"{ResourcePath}/{id}");
    }

    protected override ClienteViewModel ToViewModel(ClienteRestDto source) {
        return new ClienteViewModel {
            Id = source.Id,
            Activo = source.Activo,
            Cuenta = ParseCuenta(source.Cuenta),
            Nombres = source.Nombres ?? string.Empty,
            Correo = source.Correo ?? string.Empty,
            Telefono = source.Telefono ?? string.Empty,
            Calificacion = source.Calificacion
        };
    }

    private CuentaUsuarioViewModel ParseCuenta(Rest.Dtos.Cuentas.CuentaUsuarioRestDto? Cuenta)
    {
        if (Cuenta is null) {
            return new CuentaUsuarioViewModel();
        }
        return new CuentaUsuarioViewModel
        {
            Activo = Cuenta.Activo,
            fechaBloqueo = Cuenta.fechaBloqueo,
            Id = Cuenta.Id,
            IntentosFallidos = Cuenta.IntentosFallidos,
            Password = Cuenta.Password,
            Rol = ParseEnum<RolEnum>(Cuenta.Rol, RolEnum.NO_ADMITIDO),
            UltimaSesion = Cuenta.UltimaSesion,
            UserName = Cuenta.UserName
        };
    }

    protected override ClienteRestDto ToRest(ClienteViewModel source)
    {
        return new ClienteRestDto {
            Id = source.Id,
            Nombres = source.Nombres.Trim(),
            Correo = source.Correo.Trim(),
            Telefono = source.Telefono.Trim(),
            Cuenta = ParseCuenta(source.Cuenta)
        };
    }

    private CuentaUsuarioRestDto ParseCuenta(CuentaUsuarioViewModel? Cuenta)
    {
        if (Cuenta is null) {
            return new CuentaUsuarioRestDto();
        }

        return new CuentaUsuarioRestDto {
            Activo = Cuenta.Activo,
            fechaBloqueo = Cuenta.fechaBloqueo,
            Id = Cuenta.Id,
            IntentosFallidos = Cuenta.IntentosFallidos,
            Password = Cuenta.Password,
            Rol = Cuenta.Rol.ToString(),
            UltimaSesion = DateTime.Now,
            UserName = Cuenta.UserName
        };
    }

    private static RolEnum ParseClienteRol(string? source) {
        if(string.IsNullOrWhiteSpace(source)) {
            return RolEnum.CLIENTE;
        }
        if(string.Equals(source, "CLIENTE", StringComparison.OrdinalIgnoreCase)) {
            return RolEnum.CLIENTE;
        }
        return RolEnum.NO_ADMITIDO; // Valor por defecto si no se reconoce el rol.
    }
}