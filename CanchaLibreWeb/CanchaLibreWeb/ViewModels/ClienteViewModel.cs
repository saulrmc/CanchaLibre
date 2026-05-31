using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class ClienteViewModel : UsuarioViewModel
{
    public RolEnum Rol = RolEnum.CLIENTE;
    public List<ReservaViewModel> HistorialReservas {get; set;}
    public int Calificacion {get; set;}

}
