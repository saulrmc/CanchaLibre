using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
public sealed class PropietarioRestDto
{
    public int Id {get; set;}
    public String Nombres { get; set; } = string.Empty;
    public CuentaUsuarioRestDto? Cuenta {get; set;} 
    public String Correo { get; set; } = string.Empty;
    public String Telefono { get; set; } = string.Empty;
    public bool Activo { get; set; }
    public int Calificacion {get; set;}
    public string Ruc { get; set; } = string.Empty;

}