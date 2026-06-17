namespace CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;
public sealed class ReservaRestDto {
    public int idReserva { get; set; }
    public DateTime fechaHora { get; set; }
    public string? estado { get; set; } = string.Empty;
    public int IdCliente { get; set; }
    public int IdCancha { get; set; }
    public PagoRestDto? Pago { get; set; }
    public DateTime duracion { get; set; }
}