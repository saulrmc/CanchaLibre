using System.ComponentModel.DataAnnotations;

namespace CanchaLibreWeb.ViewModels;
public enum EstadoBloqueEnum {
    DISPONIBLE = 1,
    RESERVADO = 2,
    BLOQUEADO = 3, //cerrado manualmente por propietario, propietario no declaro precio para bloque
    MANTENIMIENTO = 4
}