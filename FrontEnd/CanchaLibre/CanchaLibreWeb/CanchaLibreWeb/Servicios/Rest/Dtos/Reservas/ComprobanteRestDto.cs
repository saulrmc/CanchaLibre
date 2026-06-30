using System.Text.Json.Serialization;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

public sealed class ComprobanteRestDto
{
    [JsonPropertyName("idComprobante")] public int idComprobante {get; set;}
    [JsonPropertyName("serie")] public string serie {get; set;} = string.Empty;
    [JsonPropertyName("numero")] public string numero {get; set;} = string.Empty;
    [JsonPropertyName("fechaEmision")] public DateTime? FechaEmision {get; set;}
    [JsonPropertyName("montoBloques")] public double montoBloques {get; set;}
}
