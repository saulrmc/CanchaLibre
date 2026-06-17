package pe.edu.pucp.canchalibre.modelo.cancha;

public enum EstadoBloque {
    DISPONIBLE,
    RESERVADO,
    BLOQUEADO, //cerrado manualmente por propietario, propietario no declaro precio para bloque
    MANTENIMIENTO
}
