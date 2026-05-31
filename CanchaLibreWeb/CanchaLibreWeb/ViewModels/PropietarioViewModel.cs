using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class PropietarioViewModel : UsuarioViewModel
{
    public RolEnum Rol = RolEnum.PROPIETARIO;
    public List<CanchaViewModel>? Canchas {get; set;}
    public int Calificacion {get; set;}
    
}