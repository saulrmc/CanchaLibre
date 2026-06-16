namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

public sealed class ClienteRestDto {
    public int Id { get; set; }
    public string Nombres { get; set; } = string.Empty;
    public string Contraseña { get; set; } = string.Empty;
    public string? Correo { get; set; }
    public string? Telefono { get; set; }
    public CuentaUsuarioRestDto? CuentaUsuario { get; set; }
    public int intentosFallidos { get; set; }
    public DateTime ultimaSesion; 
    public string? Rol { get; set; } = string.Empty;
    public List<ReservaRestDto> historialReservas { get; set; } = new List<ReservaRestDto>();
    public int calificacion { get; set; }
    public List<CanchaRestDto> canchas { get; set; } = new List<CanchaRestDto>();

}