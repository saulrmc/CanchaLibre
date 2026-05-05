package pe.edu.pucp.CanchaLibre.modelo.resena;

import pe.edu.pucp.CanchaLibre.modelo.cancha.Cancha;
import pe.edu.pucp.CanchaLibre.modelo.usuario.Cliente;

import java.time.LocalDateTime;

public class Resena{
	private int idResena;
	private String descripcion;
	private int calificacion;
	private LocalDateTime fechaPublicacion;
	private Cliente cliente;
	private Cancha cancha;//error mío al considerar que las reseñas
	// eran para los usuarios cuando eso era para las calificaciones.
	// Las reseñas son exclusivas de las canchas


	public Cancha getCancha() {
		return cancha;
	}

	public void setCancha(Cancha cancha) {
		this.cancha = cancha;
	}


	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
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
