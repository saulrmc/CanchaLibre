package pe.edu.pucp.CanchaLibre.modelo.Reserva;
import pe.edu.pucp.CanchaLibre.modelo.Cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.Transaccion.Pago;
import pe.edu.pucp.CanchaLibre.modelo.Usuario.Cliente;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {
    private int idReserva;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoReserva estado;
    private Cliente cliente;
    private Cancha cancha;
    private Pago pago;

    
}
