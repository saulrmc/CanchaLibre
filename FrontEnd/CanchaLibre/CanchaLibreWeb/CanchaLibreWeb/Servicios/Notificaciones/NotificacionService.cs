namespace CanchaLibreWeb.Servicios.Notificaciones;

public sealed class ReservaExitosaInfo
{
    public string NombreCancha { get; init; } = string.Empty;
    public string NombreCliente { get; init; } = string.Empty;
    public DateTime Fecha { get; init; } = DateTime.Now;
}

// Singleton: el único modo de que un circuito (cliente) avise a otro circuito (admin)
public sealed class NotificacionService
{
    public event Action<ReservaExitosaInfo>? ReservaConfirmada;

    public void NotificarReservaExitosa(ReservaExitosaInfo info)
    {
        ReservaConfirmada?.Invoke(info);
    }
}