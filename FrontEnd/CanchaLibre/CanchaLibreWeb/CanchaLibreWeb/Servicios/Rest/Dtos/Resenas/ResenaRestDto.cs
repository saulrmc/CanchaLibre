namespace CanchaLibreWeb.Servicios.Rest.Dtos.Resenas;
public sealed class ResenaRestDto {
    public int idResena { get; set; }
	public string descripcion { get; set; } = string.Empty;
	public int calificacion { get; set; }
	public DateTime fechaPublicacion { get; set; }
	public int IdCliente { get; set; }
	public int IdCancha { get; set; }
}