using CanchaLibreWeb.Servicios.Rest;
using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

namespace CanchaLibreWeb.Servicios.Transacciones;

public class PagosServiceRestClient : IPagosServiceClient
{
    private const string ResourcePath = "pagos";
    private readonly RestClient Api;

    public PagosServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
    {
        var baseUrl = configuration["RestApiBaseUrl"]?.Trim();
        if (string.IsNullOrWhiteSpace(baseUrl))
            throw new InvalidOperationException("No se encontró configuración para 'RestApiBaseUrl'.");
        Api = RestClient.Create(httpClientFactory, baseUrl);
    }

    public PagoRestDto CrearConReserva(int idReserva, PagoRestDto pago)
    {
        return Api.Post<PagoRestDto, PagoRestDto>(
            $"{ResourcePath}/reserva/{idReserva}", pago);
    }

    public void Actualizar(int idPago, PagoRestDto pago)
    {
        Api.Put($"{ResourcePath}/{idPago}", pago);
    }
}
