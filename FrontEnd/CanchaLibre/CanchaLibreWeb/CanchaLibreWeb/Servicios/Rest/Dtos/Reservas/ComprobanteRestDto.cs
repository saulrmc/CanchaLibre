namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

public sealed class ComprobanteRestDto
{
    public int idComprobante {get; set;}
	public String serie {get; set;} = String.Empty;
    public String numero {get; set;} = String.Empty;
	public DateTime FechaEmision {get; set;}
}