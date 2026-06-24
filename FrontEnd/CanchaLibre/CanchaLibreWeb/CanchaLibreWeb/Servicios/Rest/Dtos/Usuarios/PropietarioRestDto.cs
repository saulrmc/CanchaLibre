using CanchaLibreWeb.Servicios.Rest.Dtos.Cuentas;
using System.Text.Json.Serialization;

namespace CanchaLibreWeb.Servicios.Rest.Dtos.Usuarios;
public sealed class PropietarioRestDto
{

    [JsonPropertyName("id")]
    public int Id { get; set; }

    [JsonPropertyName("nombres")]
    public string Nombres { get; set; } = string.Empty;

    [JsonPropertyName("cuentaUsuario")]
    public CuentaUsuarioRestDto? Cuenta { get; set; }

    [JsonPropertyName("correo")]
    public string Correo { get; set; } = string.Empty;

    [JsonPropertyName("telefono")]
    public string Telefono { get; set; } = string.Empty;

    [JsonPropertyName("activo")]
    public bool Activo { get; set; }

    [JsonPropertyName("calificacion")]
    public double Calificacion { get; set; }

    [JsonPropertyName("ruc")]
    public string Ruc { get; set; } = string.Empty;
}