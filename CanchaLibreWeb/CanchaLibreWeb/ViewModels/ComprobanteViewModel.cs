using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class ComprobanteViewModel
{

    public int idComprobante {get; set;}
	public double Igv {get; set;}	

    [Required(ErrorMessage = "Debe tener una fecha")]
	public DateTime FechaEmision {get; set;}

    [Required(ErrorMessage = "Debe tener una reserva asignada")]
	public ReservaViewModel? Reserva {get; set;}
}