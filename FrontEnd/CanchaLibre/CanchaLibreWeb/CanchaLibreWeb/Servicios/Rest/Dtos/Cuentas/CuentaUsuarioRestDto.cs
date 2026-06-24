using System;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;

public sealed class CuentaUsuarioRestDto
{
    [JsonPropertyName("id")]
    public int Id { get; set; }

    [JsonPropertyName("userName")]
    public string UserName { get; set; } = string.Empty;

    [JsonPropertyName("password")]
    public string Password { get; set; } = string.Empty;

    [JsonPropertyName("activo")]
    public bool Activo { get; set; }

    [JsonPropertyName("rol")]
    public string Rol { get; set; } = string.Empty;

    [JsonPropertyName("intentosFallidos")]
    public int IntentosFallidos { get; set; }

    [JsonPropertyName("ultimaSesion")]
    [JsonConverter(typeof(IsoDateTimeConverter))]
    public DateTime? UltimaSesion { get; set; }

    [JsonPropertyName("fechaBloqueo")]
    [JsonConverter(typeof(IsoDateTimeConverter))]
    public DateTime? FechaBloqueo { get; set; }
}

public class IsoDateTimeConverter : JsonConverter<DateTime?>
{
    public override DateTime? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        return reader.TryGetDateTime(out DateTime dt) ? dt : null;
    }

    public override void Write(Utf8JsonWriter writer, DateTime? value, JsonSerializerOptions options)
    {
        if (value.HasValue)
            writer.WriteStringValue(value.Value.ToString("yyyy-MM-ddTHH:mm:ss"));
        else
            writer.WriteNullValue();
    }
}