using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;

public class AdministradorViewModel : UsuarioViewModel
{
    public RolEnum Rol = RolEnum.ADMINISTRADOR;

}