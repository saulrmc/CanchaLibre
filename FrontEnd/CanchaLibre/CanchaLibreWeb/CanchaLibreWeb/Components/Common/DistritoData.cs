using System.Reflection;
using System.Text.Json;
using Microsoft.Extensions.Caching.Memory;

namespace CanchaLibreWeb.Components.Common;

public class DistritoData
{
    private readonly IMemoryCache _cache;
    private readonly string _cacheKey = "cache_distritos";

    public DistritoData(IMemoryCache memoryCache)
    {
        _cache = memoryCache;
    }
    public async Task<IEnumerable<Distrito>> GetDistritosAsync()
    => (await _cache.GetOrCreateAsync(_cacheKey, LoadData)) ?? Enumerable.Empty<Distrito>();

    private Task<IEnumerable<Distrito>> LoadData(ICacheEntry cacheEntry)
    {
        Assembly assembly = typeof(DistritoData).Assembly;
        string resourceName = "CanchaLibreWeb.Components.Resources.DistritoData.json";

        using Stream? stream = assembly.GetManifestResourceStream(resourceName);
        if (stream == null)
        {
            throw new FileNotFoundException($"No se encontró el recurso: {resourceName}");
        }

        using StreamReader reader = new(stream);
        string jsonText = reader.ReadToEnd();

        var opciones = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
        List<Distrito>? lista = JsonSerializer.Deserialize<List<Distrito>>(jsonText, opciones);

        // Si por alguna razón la deserialización falla, devolvemos una lista vacía en lugar de null
        return Task.FromResult((lista ?? new List<Distrito>()).AsEnumerable());
    }
}