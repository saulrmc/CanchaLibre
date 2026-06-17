package pe.edu.pucp.canchalibre.modelo.resena;

import pe.edu.pucp.canchalibre.modelo.cancha.Cancha;
import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import pe.edu.pucp.canchalibre.modelo.usuario.Cliente;

import java.time.LocalDateTime;
import java.util.Date;

public class Resena{
	private int idResena;
	private String descripcion;
	private int calificacion;
	private Date fechaPublicacion;
	//private Cliente cliente;
	private Reserva reserva;//Se comprueba tener una reserva para dejar resena, extraer idClliente de reserva
	private Cancha cancha;// Las reseñas son exclusivas de las canchas


	public Cancha getCancha() {
		return cancha;
	}
	public void setCancha(Cancha cancha) {
		this.cancha = cancha;
	}

	public Reserva getReserva(){ return reserva; }
	public void setReserva(Reserva reserva){ this.reserva = reserva;}

	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

//	public Cliente getCliente() {
//		return cliente;
//	}
//
//	public void setCliente(Cliente cliente) {
//		this.cliente = cliente;
//	}

	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public int getIdResena() {
		return idResena;
	}

	public void setIdResena(int idResena) {
		this.idResena = idResena;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
