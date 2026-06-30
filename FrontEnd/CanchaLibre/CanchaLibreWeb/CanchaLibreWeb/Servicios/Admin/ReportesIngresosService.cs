using CanchaLibreWeb.Servicios.Rest;

namespace CanchaLibreWeb.Servicios.Admin;

public sealed class ReportesIngresosService
{
    private readonly RestClient _api;

    public ReportesIngresosService(IConfiguration configuration, IHttpClientFactory httpClientFactory)
    {
        var baseUrl = configuration["RestApiBaseUrl"]?.Trim()
                      ?? throw new InvalidOperationException("No se encontró 'RestApiBaseUrl' en la configuración.");
        _api = RestClient.Create(httpClientFactory, baseUrl);
    }

    public List<IngresoCanchaDto> ObtenerIngresos()
    {
        return _api.Get<List<IngresoCanchaDto>>("canchas/con-ingresos");
    }
}
