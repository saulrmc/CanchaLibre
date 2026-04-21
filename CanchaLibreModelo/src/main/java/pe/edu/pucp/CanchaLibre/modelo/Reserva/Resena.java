package pe.edu.pucp.CanchaLibre.modelo.Reserva;

import pe.edu.pucp.CanchaLibre.modelo.Usuario.Usuario;

import java.time.LocalDateTime;

public class Resena{
	private int idResena;
	private String descripcion;
	private int calificacion;
	private LocalDateTime fechaPublicacion;
	private Usuario destinatario; //aunque en la práctica no le podría
	//enviar una reseña al administrador


	public int getCalificacion() {
		return calificacion;
	}

	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
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
