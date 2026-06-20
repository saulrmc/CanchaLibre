namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
public sealed class ClienteRestDto { 
    public int IdUsuario { get; set; }
    public string? Nombres { get; set; } = string.Empty;
    public string? Contrasena { get; set; } = string.Empty;
    public string? Correo { get; set; } = string.Empty;
    public string? Telefono { get; set; } = string.Empty;
    public int IntentosFallidos { get; set; } 
    public DateTime UltimaSesion { get; set; }

    public List<ReservaRestDto> historialReservas { get; set; } = new List<ReservaRestDto>();
    public int calificacion { get; set; } 
}