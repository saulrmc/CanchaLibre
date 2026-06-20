using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public class PropietariosServiceRestClient : BaseRestServiceClient<PropietarioViewModel, PropietarioRestDto>, IPropietariosServiceClient {
    public PropietariosServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) {
        // IConfiguration e IHttpClientFactory son inyectados por el contenedor de DI.
    }

    public List<PropietarioViewModel> Listar() {
        var payload = Api.Get<List<PropietarioRestDto>>("v1/propietarios");

        var response = new List<PropietarioViewModel>(payload.Count);
        foreach (var item in payload) {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public PropietarioViewModel? Obtener(int id) {
        try {
            var payload = Api.Get<PropietarioRestDto>($"v1/propietarios/{id}");
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }
    public PropietarioViewModel? BuscarPorNombre(string nombre) {
        try {
            var path = $"v1/propietarios/nombre/{Uri.EscapeDataString(nombre)}";
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
                Api.Post("v1/propietarios", payload);
                break;
            case Estado.Modificado:
                Api.Put($"v1/propietarios/{modelo.Id}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id) {
        Api.Delete($"v1/propietarios/{id}");
    }

    protected override PropietarioViewModel ToViewModel(PropietarioRestDto source) {
        return new PropietarioViewModel {
            Id = source.IdUsuario,
            Nombres = source.Nombres ?? string.Empty,
            Contrasena = string.Empty, // No se expone la contraseña en el ViewModel por seguridad.
            Correo = source.Correo ?? string.Empty,
            Telefono = source.Telefono ?? string.Empty,
            IntentosFallidos = source.IntentosFallidos,
            UltimaSesion = source.UltimaSesion,
            Canchas = source.canchas?.Select(c => new CanchaViewModel {
                idCancha = c.idCancha,
                nombre = c.nombre,
                descripcion = c.descripcion,
                direccion = c.direccion,
                imagenUrl = c.imagenUrl,
                disponible = c.disponible,
                deportes = ParseEnumDeportes(c.deportes),
            }).ToList() ?? new List<CanchaViewModel>(),
            Calificacion = source.Calificacion
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
            IdUsuario = source.Id,
            Nombres = source.Nombres.Trim(),
            Contrasena = source.Contrasena, // Se asume que el ViewModel ya tiene la contraseña en texto plano (nueva o sin cambios).
            Correo = source.Correo.Trim(),
            Telefono = source.Telefono.Trim(),
            IntentosFallidos = source.IntentosFallidos,
            UltimaSesion = source.UltimaSesion,
            canchas = source.Canchas?.Select(c => new CanchaRestDto {
                idCancha = c.idCancha,
                nombre = c.nombre,
                descripcion = c.descripcion,
                direccion = c.direccion,
                imagenUrl = c.imagenUrl,
                disponible = c.disponible,
                deportes = ParseStringDeportes(c.deportes),
            }).ToList() ?? new List<CanchaRestDto>(),
            Calificacion = source.Calificacion
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