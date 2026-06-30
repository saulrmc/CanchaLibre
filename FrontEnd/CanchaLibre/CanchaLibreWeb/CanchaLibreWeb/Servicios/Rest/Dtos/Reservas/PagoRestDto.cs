using System.Text.Json.Serialization;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class PagoRestDto {
    [JsonPropertyName("idPago")] public int idPago {get; set;}
    [JsonPropertyName("metodoPago")] public string metodoPago {get; set;} = string.Empty;
    [JsonPropertyName("monto")] public double monto {get; set;}
    [JsonPropertyName("fechaPago")] public DateTime? fechaPago {get; set;}
    [JsonPropertyName("comprobante")] public ComprobanteRestDto? comprobante {get; set;}
}
