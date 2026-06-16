using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class ReservaViewModel
{
    public int idReserva {get; set;}
    public DateTime fechaHora {get; set;}
    public EstadoReservaEnum estado {get; set;}

    // No es que la Reserva deba tener un cliente asociado,
    //  sino que el cliente es quien hace la reserva, 
    // por lo que se asignará automáticamente al momento de crear la reserva
    // [Required(ErrorMessage = "Debe tener un cliente")]
    // public ClienteViewModel? cliente {get; set;}

    [Required(ErrorMessage = "Debe tener una cancha")]
    public CanchaViewModel? cancha {get; set;}

    [Required(ErrorMessage = "Debe tener un metodo de pago")]
    public PagoViewModel? pago {get; set;}

    [Required(ErrorMessage = "Debe tener una duracion")]
    public DateTime duracion {get; set;}

    public ComprobanteViewModel? comprobante {get; set;}
}