package pe.edu.pucp.CanchaLibre.modelo.Transaccion;

import pe.edu.pucp.CanchaLibre.modelo.Reserva.Reserva;

import java.time.LocalDateTime;

public class Comprobante{
	private int idComprobante;
	private int idReserva;
	private int idUsuario;
	private String nombreUsuario; //no se coloca a una clase Usuario
	//porque no es relevante saber su contraseña, ni los intentos fallidos de inicio de
	//sesión que lleva
	private String correoUsuario;
	private double igv;
	private double precioHora;
	private LocalDateTime fechaEmision;
	private Reserva reserva;

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public LocalDateTime getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(LocalDateTime fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public double getPrecioHora() {
		return precioHora;
	}

	public void setPrecioHora(double precioHora) {
		this.precioHora = precioHora;
	}

	public double getIgv() {
		return igv;
	}

	public void setIgv(double igv) {
		this.igv = igv;
	}

	public String getCorreoUsuario() {
		return correoUsuario;
	}

	public void setCorreoUsuario(String correoUsuario) {
		this.correoUsuario = correoUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public int getIdComprobante() {
		return idComprobante;
	}

	public void setIdComprobante(int idComprobante) {
		this.idComprobante = idComprobante;
	}
}
