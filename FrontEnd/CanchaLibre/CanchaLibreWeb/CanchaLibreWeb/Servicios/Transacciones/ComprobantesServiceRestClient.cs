using CanchaLibreWeb.Servicios.Rest;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

namespace CanchaLibreWeb.Servicios.Transacciones;

public class ComprobantesServiceRestClient : IComprobantesServiceClient
{
    private const string ResourcePath = "comprobantes";
    private readonly RestClient Api;

    public ComprobantesServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
    {
        var baseUrl = configuration["RestApiBaseUrl"]?.Trim();
        if (string.IsNullOrWhiteSpace(baseUrl))
            throw new InvalidOperationException("No se encontró configuración para 'RestApiBaseUrl'.");
        Api = RestClient.Create(httpClientFactory, baseUrl);
    }

    public ComprobanteRestDto CrearConReserva(int idReserva, ComprobanteRestDto comprobante)
    {
        return Api.Post<ComprobanteRestDto, ComprobanteRestDto>(
            $"{ResourcePath}/reserva/{idReserva}", comprobante);
    }
}
