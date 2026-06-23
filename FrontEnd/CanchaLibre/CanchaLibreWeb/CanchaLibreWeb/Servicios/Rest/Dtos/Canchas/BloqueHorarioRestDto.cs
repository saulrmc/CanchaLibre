namespace CanchaLibreWeb.Servicios.Rest.Dtos.Canchas;

public sealed class BloqueHorarioRestDto
{
    public String diaSemana {get; set;} = String.Empty;
    public TimeOnly horaInicio {get; set;}
    public TimeOnly horaFin {get; set;}
    public double precio {get; set;}
    public String estadoBloque {get; set;} = String.Empty;
}