using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public class PropietariosServiceRestClient : BaseRestServiceClient<PropietarioViewModel, PropietarioRestDto>, IPropietariosServiceClient {
    public PropietariosServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) {
        // IConfiguration e IHttpClientFactory son inyectados por el contenedor de DI.
    }
    private const string ResourcePath = "propietarios";
    public List<PropietarioViewModel> Listar() {
        var payload = Api.Get<List<PropietarioRestDto>>($"{ResourcePath}");

        var response = new List<PropietarioViewModel>(payload.Count);
        foreach (var item in payload) {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public PropietarioViewModel? Obtener(int id) {
        try {
            var payload = Api.Get<PropietarioRestDto>($"{ResourcePath}/{id}");
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }
    public PropietarioViewModel? BuscarPorNombre(string nombre) {
        try {
            var path = $"{ResourcePath}/nombre/{Uri.EscapeDataString(nombre)}";
            var payload = Api.Get<PropietarioRestDto>(path);
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }

    public void Guardar(PropietarioViewModel modelo, Estado estado) {
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

    protected override PropietarioViewModel ToViewModel(PropietarioRestDto source) {
        return new PropietarioViewModel {
            Id = source.Id,
            Activo = source.Activo,
            Correo = source.Correo,
            Cuenta = ParseCuenta(source.Cuenta),
            Nombres = source.Nombres,
            Ruc = source.Ruc,
            Telefono = source.Telefono,
            Calificacion = source.Calificacion
        };
    }

    private CuentaUsuarioViewModel ParseCuenta(CuentaUsuarioRestDto? Cuenta)
    {
        if (Cuenta is null) {
            return new CuentaUsuarioViewModel();
        }

        return new CuentaUsuarioViewModel
        {
            Activo = Cuenta.Activo,
            FechaBloqueo = Cuenta.FechaBloqueo ?? DateTime.MinValue,
            Id = Cuenta.Id,
            IntentosFallidos = Cuenta.IntentosFallidos,
            Password = Cuenta.Password,
            Rol = ParseEnum<RolEnum>(Cuenta.Rol, RolEnum.NO_ADMITIDO),
            UltimaSesion = Cuenta.UltimaSesion ?? DateTime.MinValue,
            UserName = Cuenta.UserName
        };
    }

    private List<DeporteEnum> ParseEnumDeportes(List<string> deportes)
    {
        var result = new List<DeporteEnum>();
        foreach (var deporte in deportes) {
            if (Enum.TryParse<DeporteEnum>(deporte, true, out var parsed)) {
                result.Add(parsed);
            }
        }
        return result;
    }

    protected override PropietarioRestDto ToRest(PropietarioViewModel source) {
        return new PropietarioRestDto {
            Id = source.Id,
            Activo = source.Activo,
            Calificacion = source.Calificacion,
            Correo = source.Correo,
            Cuenta = ParseCuenta(source.Cuenta),
            Nombres = source.Nombres,
            Ruc = source.Ruc,
            Telefono = source.Telefono
        };  
    }

    private CuentaUsuarioRestDto ParseCuenta(CuentaUsuarioViewModel? Cuenta)
    {
        if (Cuenta is null) {
            return new Rest.Dtos.Cuentas.CuentaUsuarioRestDto();
        }

        return new CuentaUsuarioRestDto {
            Activo = Cuenta.Activo,
            FechaBloqueo = Cuenta.FechaBloqueo,
            Id = Cuenta.Id,
            IntentosFallidos = Cuenta.IntentosFallidos,
            Password = Cuenta.Password,
            Rol = Cuenta.Rol.ToString(),
            UltimaSesion = Cuenta.UltimaSesion,
            UserName = Cuenta.UserName
        };
    }

    private List<string> ParseStringDeportes(List<DeporteEnum>? deportes)
    {
        if (deportes == null) {
            return new List<string>();
        }
        var result = new List<string>();
        foreach (var deporte in deportes) {
            result.Add(deporte.ToString());
        }
        return result;
    }

    private static RolEnum ParsePropietarioRol(string? source) {
        if(string.IsNullOrWhiteSpace(source)) {
            return RolEnum.PROPIETARIO;
        }
        if(string.Equals(source, "PROPIETARIO", StringComparison.OrdinalIgnoreCase)) {
            return RolEnum.PROPIETARIO;
        }
        return RolEnum.NO_ADMITIDO; // Valor por defecto si no se reconoce el rol.
    }
}