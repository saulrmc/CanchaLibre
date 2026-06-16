namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

public sealed class ReservaRestDto {
    public int Id { get; set; }
    public DateTime FechaHora { get; set; }
    public string? Estado { get; set; } = string.Empty;
    public CanchaRestDto? Cancha { get; set; }
    public PagoRestDto? Pago { get; set; }
    public DateTime duracion { get; set; }

}