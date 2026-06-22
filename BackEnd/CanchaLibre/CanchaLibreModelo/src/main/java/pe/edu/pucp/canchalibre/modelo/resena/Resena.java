package pe.edu.pucp.canchalibre.modelo.resena;

import pe.edu.pucp.canchalibre.modelo.reserva.Reserva;
import java.time.LocalDateTime;

public class Resena {
	private int idResena;
	private String descripcion;
	private double calificacion;
	private LocalDateTime fechaPublicacion;
	private Reserva reserva;

	public int getIdResena() {
		return idResena;
	}

	public void setIdResena(int idResena) {
		this.idResena = idResena;
	}

	public Reserva getReserva(){ return reserva; }
	public void setReserva(Reserva reserva){ this.reserva = reserva;}

	public double getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(double calificacion) {
		this.calificacion = calificacion;
	}

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
