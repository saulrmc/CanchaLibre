namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class PagoRestDto {
    public int id{ get; set; }
    public string? metodoPago{ get; set; } = string.Empty;
    public double monto{ get; set; }
    public DateTime fechaPago{ get; set; }

    public int IdReserva{ get; set; } 
}
