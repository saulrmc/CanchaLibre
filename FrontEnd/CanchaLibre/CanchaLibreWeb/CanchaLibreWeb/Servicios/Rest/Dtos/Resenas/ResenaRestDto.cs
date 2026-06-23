using CanchaLibreWeb.Servicios.Rest.Dtos.Reservas;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Resenas;
public sealed class ResenaRestDto {
    public int idResena {get; set;}
	public String descripcion { get; set; } = string.Empty;
	public int calificacion { get; set; }

	public DateTime fechaPublicacion { get; set; }
	public ReservaRestDto? reserva { get; set; }
}