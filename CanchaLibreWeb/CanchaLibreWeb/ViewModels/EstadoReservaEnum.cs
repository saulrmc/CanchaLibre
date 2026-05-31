using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public enum EstadoReservaEnum{
	ESPERA = 1, 
    PAGADO = 2,
    CANCELADO = 3,
    COMPLETADO = 4
}