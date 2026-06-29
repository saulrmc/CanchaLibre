using System.Text.Json.Serialization;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;

public sealed class BloqueHorarioRestDto
{
    public int id {get; set;}
    [JsonPropertyName("dia")]
    public String diaSemana {get; set;} = String.Empty;
    public TimeOnly horaInicio {get; set;}
    public TimeOnly horaFin {get; set;}
    public double precio {get; set;}
    [JsonPropertyName("estado")]
    public String estadoBloque {get; set;} = String.Empty;
}