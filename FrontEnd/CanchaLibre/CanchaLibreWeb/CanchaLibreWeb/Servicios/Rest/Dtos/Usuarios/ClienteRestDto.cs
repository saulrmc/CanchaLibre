using System.Text.Json.Serialization;
using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;

public sealed class ClienteRestDto
{
    [JsonPropertyName("id")]
    public int Id { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("cuentaUsuario")]
    public CuentaUsuarioRestDto? CuentaUsuario { get; set; }

    [JsonPropertyName("correo")]
    public string Correo { get; set; } = string.Empty;

    [JsonPropertyName("telefono")]
    public string Telefono { get; set; } = string.Empty;

    [JsonPropertyName("activo")]
    public bool Activo { get; set; }

    [JsonPropertyName("calificacion")]
    public double Calificacion { get; set; }
}