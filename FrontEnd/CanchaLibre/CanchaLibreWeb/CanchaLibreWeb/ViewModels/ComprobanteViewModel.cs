using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class ComprobanteViewModel
{

    public int idComprobante {get; set;}
	public String serie {get; set;} = String.Empty;
    public String numero {get; set;} = String.Empty;

    //[Required(ErrorMessage = "Debe tener una fecha")] //no se genera automaticamente????
	public DateTime FechaEmision {get; set;}
    public double montoBloques {get; set;}

}