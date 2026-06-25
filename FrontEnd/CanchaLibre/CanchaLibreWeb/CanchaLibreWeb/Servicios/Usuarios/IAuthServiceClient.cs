using System.Text.Json;

namespace CanchaLibreWeb.Servicios.Usuarios;

public interface IAuthServiceClient
{
    (int Id, string Nombres, string Correo, string? Rol) ValidarCredenciales(string correo, string contrasena);
    bool SolicitarRecuperacion(string correo);
}

public class AuthServiceRestClient : IAuthServiceClient
{
    private readonly HttpClient _httpClient;

    public AuthServiceRestClient(IConfiguration configuration, IHttpClientFactory httpClientFactory)
    {
        var baseUrl = configuration["RestApiBaseUrl"]?.Trim()
            ?? throw new InvalidOperationException("No se encontró 'RestApiBaseUrl' en la configuración.");
        _httpClient = httpClientFactory.CreateClient();
        _httpClient.BaseAddress = new Uri(baseUrl.EndsWith('/') ? baseUrl : baseUrl + "/");
    }

    public (int Id, string Nombres, string Correo, string? Rol) ValidarCredenciales(string correo, string contrasena)
    {
        var payload = new { userName = correo, password = contrasena };
        var requestJson = JsonSerializer.Serialize(payload);
        var content = new StringContent(requestJson, System.Text.Encoding.UTF8, "application/json");

        var response = _httpClient.PostAsync("cuentas/login", content)
            .GetAwaiter().GetResult();

        if (!response.IsSuccessStatusCode)
            return (0, string.Empty, string.Empty, null);

        var json = response.Content.ReadFromJsonAsync<JsonElement>().GetAwaiter().GetResult();
        int id = json.GetProperty("id").GetInt32();
        string nombres = json.GetProperty("nombres").GetString() ?? string.Empty;
        string correoResp = json.GetProperty("correo").GetString() ?? string.Empty;
        string rol = json.GetProperty("rol").GetString() ?? string.Empty;

        return (id, nombres, correoResp, rol);
    }

    public bool SolicitarRecuperacion(string correo)
    {
        try
        {
            var response = _httpClient.GetAsync("clientes")
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
            {
                var json = response.Content.ReadFromJsonAsync<JsonElement>().GetAwaiter().GetResult();
                if (json.ValueKind == JsonValueKind.Array)
                    foreach (var item in json.EnumerateArray())
                        if (item.TryGetProperty("correo", out var c) &&
                            c.GetString()?.Equals(correo, StringComparison.OrdinalIgnoreCase) == true)
                            return true;
            }

            response = _httpClient.GetAsync("propietarios")
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
            {
                var json = response.Content.ReadFromJsonAsync<JsonElement>().GetAwaiter().GetResult();
                if (json.ValueKind == JsonValueKind.Array)
                    foreach (var item in json.EnumerateArray())
                        if (item.TryGetProperty("correo", out var c) &&
                            c.GetString()?.Equals(correo, StringComparison.OrdinalIgnoreCase) == true)
                            return true;
            }

            response = _httpClient.GetAsync("administradores")
                .GetAwaiter().GetResult();
            if (response.IsSuccessStatusCode)
            {
                var json = response.Content.ReadFromJsonAsync<JsonElement>().GetAwaiter().GetResult();
                if (json.ValueKind == JsonValueKind.Array)
                    foreach (var item in json.EnumerateArray())
                        if (item.TryGetProperty("correo", out var c) &&
                            c.GetString()?.Equals(correo, StringComparison.OrdinalIgnoreCase) == true)
                            return true;
            }

            return false;
        }
        catch
        {
            return false;
        }
    }
}
