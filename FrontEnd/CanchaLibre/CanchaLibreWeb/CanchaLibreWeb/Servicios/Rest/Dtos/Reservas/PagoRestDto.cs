namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class PagoRestDto {
    public int idPago {get; set;}
    public String metodoPago {get; set;} = String.Empty;
    public double monto {get; set;}
    public DateTime fechaPago {get; set;}
    public ComprobanteRestDto? comprobante {get; set;}
}
