namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

public sealed class ClienteRestDto
{
    public int IdUsuario { get; set; }
    public string? Nombres { get; set; } = string.Empty;
    public string? Correo { get; set; } = string.Empty;
    public string? Telefono { get; set; } = string.Empty;

    public CuentaUsuarioRestDto CuentaUsuario { get; set; } = new();

    public int IntentosFallidos { get; set; }
    public DateTime UltimaSesion { get; set; }

    public List<ReservaRestDto> historialReservas { get; set; } = new();
    public int calificacion { get; set; }

    public sealed class CuentaUsuarioRestDto
    {
        public string? UserName { get; set; }
        public string? Password { get; set; }
    }
}