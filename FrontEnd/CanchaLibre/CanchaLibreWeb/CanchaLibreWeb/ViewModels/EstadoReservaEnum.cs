using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public enum EstadoReservaEnum{
	PENDIENTE_PAGO = 1, 
    CONFIRMADA = 2,
    CANCELADA = 3,
    RECHAZADA = 4
}