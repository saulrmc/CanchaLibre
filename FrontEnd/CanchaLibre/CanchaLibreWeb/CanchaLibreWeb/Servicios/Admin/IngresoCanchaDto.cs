namespace CanchaLibreWeb.Servicios.Admin;

public sealed class IngresoCanchaDto
{
    public int Id { get; set; }
    public string Nombre { get; set; } = string.Empty;
    public double Ingresos { get; set; }
    public int Reservas { get; set; }
    public int HorasOcupacion { get; set; }
}
