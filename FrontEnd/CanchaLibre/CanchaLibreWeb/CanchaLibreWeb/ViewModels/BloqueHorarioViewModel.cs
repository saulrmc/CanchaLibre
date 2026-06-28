using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class BloqueHorarioViewModel
{
    [Required(ErrorMessage = "Debe ingresar un día de semana")]
    public DiaSemanaEnum diaSemana {get; set;}

    [Required(ErrorMessage = "Debe ingresar una hora de inicio")]
    public TimeOnly horaInicio {get; set;}

    [Required(ErrorMessage = "Debe ingresar una hora de fin")]
    public TimeOnly horaFin {get; set;}

    [Required(ErrorMessage = "Debe ingresar un precio")]
    public double precio {get; set;}
    public EstadoBloqueEnum estadoBloque {get; set;}

}