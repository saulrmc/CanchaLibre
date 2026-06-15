using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class NotificacionCompViewModel : NotificacionViewModel
{
    public ComprobanteViewModel? comprobante {get; set;}
    public String descripcionComprobante {get; set;} = string.Empty;
}