namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

public class UsuarioRespuestaRestDto
{
    public int IdUsuario { get; set; }
    public string Nombres { get; set; } = string.Empty;
    public string Correo { get; set; } = string.Empty;
    public string Contrasena { get; set; } = string.Empty; 
}