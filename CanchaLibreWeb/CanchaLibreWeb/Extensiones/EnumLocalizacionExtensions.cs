using CanchaLibreWeb.ViewModels;

namespace CanchaLibreWeb.Extensiones;

public static class EnumLocalizacionExtensions
{
    public static string ToLocalizedText(this RolEnum valor)
    {
        return valor switch
        {
            RolEnum.ADMINISTRADOR => "Administrador",
            RolEnum.PROPIETARIO => "Propietario",
            RolEnum.CLIENTE => "Cliente",
            _ => valor.ToString()
        };
    }
}
