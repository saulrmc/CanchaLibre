using System.Text.Json.Serialization;
using CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;
using CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class ReservaRestDto {
    [JsonPropertyName("id")]
    public int idReserva {get; set;}
    public String estado {get; set;} = String.Empty;
    public ClienteRestDto? cliente {get; set;}
    public CanchaRestDto? cancha {get; set;}
    [JsonPropertyName("pago")]
    public PagoRestDto? pago {get; set;}
    [JsonPropertyName("bloquesSeleccionados")]
    public List<BloqueHorarioRestDto>? bloques {get; set;}
    [JsonPropertyName("fechaCreacion")]
    public DateTime? FechaCreacion {get; set;}
    [JsonPropertyName("activo")]
    public bool activo {get; set;}
}