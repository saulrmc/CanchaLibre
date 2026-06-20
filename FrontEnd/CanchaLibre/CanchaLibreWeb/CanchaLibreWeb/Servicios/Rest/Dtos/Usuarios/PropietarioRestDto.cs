namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
public sealed class PropietarioRestDto
{
    public int IdUsuario { get; set; }
    public string? Nombres { get; set; } = string.Empty;
    public string? Contrasena { get; set; } = string.Empty;
    public string? Correo { get; set; } = string.Empty;
    public string? Telefono { get; set; } = string.Empty;
    public int IntentosFallidos { get; set; } 
    public DateTime UltimaSesion { get; set; }
    public List<CanchaRestDto>? canchas { get; set; }
    public int Calificacion { get; set; }
}