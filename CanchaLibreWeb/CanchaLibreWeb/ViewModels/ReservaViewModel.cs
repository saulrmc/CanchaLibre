using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class ReservaViewModel
{
    public int idReserva {get; set;}
    public DateTime fechaHora {get; set;}
    public EstadoReservaEnum estado {get; set;}

    [Required(ErrorMessage = "Debe tener un cliente")]
    public ClienteViewModel? cliente {get; set;}

    [Required(ErrorMessage = "Debe tener una cancha")]
    public CanchaViewModel? cancha {get; set;}

    [Required(ErrorMessage = "Debe tener un metodo de pago")]
    public PagoViewModel? pago {get; set;}

    [Required(ErrorMessage = "Debe tener una duracion")]
    public DateTime duracion {get; set;}
}