using System.Net;
using CanchaLibreWeb.Servicios.Base;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Servicios.Usuarios;

public class ClientesServiceRestClient : BaseRestServiceClient<ClienteViewModel, ClienteRestDto>, IClientesServiceClient {
    public ClientesServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
        : base(configuration, httpClientFactory) {
        // IConfiguration e IHttpClientFactory son inyectados por el contenedor de DI.
    }

    public List<ClienteViewModel> Listar() {
        var payload = Api.Get<List<ClienteRestDto>>("v1/clientes");

        var response = new List<ClienteViewModel>(payload.Count);
        foreach (var item in payload) {
            response.Add(ToViewModel(item));
        }

        return response;
    }

    public ClienteViewModel? Obtener(int id) {
        try {
            var payload = Api.Get<ClienteRestDto>($"v1/clientes/{id}");
            return ToViewModel(payload);
        } catch (HttpRequestException ex) when (ex.StatusCode == HttpStatusCode.NotFound) {
            return null;
        }
    }
    public ClienteViewModel? BuscarPorNombre(string nombre) {
        try {
            var path = $"v1/clientes/nombre/{Uri.EscapeDataString(nombre)}";
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
                Api.Post("v1/clientes", payload);
                break;
            case Estado.Modificado:
                Api.Put($"v1/clientes/{modelo.Id}", payload);
                break;
            default:
                throw new InvalidOperationException($"Estado no soportado: {estado}");
        }
    }

    public void Eliminar(int id) {
        Api.Delete($"/clientes/{id}");
    }

    protected override ClienteViewModel ToViewModel(ClienteRestDto source) {
        return new ClienteViewModel {
            Id = source.IdUsuario,
            Nombres = source.Nombres ?? string.Empty,
            Contrasena = string.Empty, // No se expone la contraseña en el ViewModel por seguridad.
            Correo = source.Correo ?? string.Empty,
            Telefono = source.Telefono ?? string.Empty,
            IntentosFallidos = source.IntentosFallidos,
            UltimaSesion = source.UltimaSesion,
            HistorialReservas = source.historialReservas.Select(r => new ReservaViewModel {
                idReserva = r.idReserva,
                fechaHora = r.fechaHora
            }).ToList() ?? new List<ReservaViewModel>(),
            Calificacion = source.calificacion
        };
    }

    protected override ClienteRestDto ToRest(ClienteViewModel source) {
        return new ClienteRestDto {
            IdUsuario = source.Id,
            Nombres = source.Nombres.Trim(),
            Contrasena = source.Contrasena, // Se asume que el ViewModel ya tiene la contraseña en texto plano (nueva o sin cambios).
            Correo = source.Correo.Trim(),
            Telefono = source.Telefono.Trim(),
            IntentosFallidos = source.IntentosFallidos,
            UltimaSesion = source.UltimaSesion,
            historialReservas = source.HistorialReservas?.Select(r => new ReservaRestDto {
                idReserva = r.idReserva,
                fechaHora = r.fechaHora
            }).ToList() ?? new List<ReservaRestDto>(),
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