using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class PagoViewModel
{
    public int idPago {get; set;}

    [Required(ErrorMessage = "Debe elegir un método de pago")]
    public MetodoPagoEnum metodoPago {get; set;}

    [Required(ErrorMessage = "Debe ingresar un monto")]
    public double monto {get; set;}

    [Required(ErrorMessage = "Debe ingresar una fecha")]
    public DateTime fechaPago {get; set;}
    public ComprobanteViewModel? comprobante {get; set;}
}