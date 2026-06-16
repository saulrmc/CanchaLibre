namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
public sealed class PagoRestDto {
    public int Id { get; set; }
    public decimal Monto { get; set; }
    public DateTime Fecha { get; set; }
    public string? MetodoPago { get; set; } = string.Empty;
}