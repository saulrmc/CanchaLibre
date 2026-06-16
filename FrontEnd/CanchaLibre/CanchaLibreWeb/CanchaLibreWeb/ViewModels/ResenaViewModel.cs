using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class ResenaViewModel
{
    public int idResena {get; set;}

    [Required(ErrorMessage = "Debe tener una descripcion")]
	public String descripcion { get; set; } = string.Empty;
	public int calificacion { get; set; }

	public DateTime fechaPublicacion { get; set; }
	public ClienteViewModel? cliente { get; set; }
	//public CanchaViewModel? cancha { get; set; }
}