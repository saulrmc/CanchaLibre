using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public class NotificacionViewModel
{
    public UsuarioViewModel? destinatario {get; set;}
    public DateTime fechaEnvio {get; set;}
}