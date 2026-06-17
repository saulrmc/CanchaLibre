package pe.edu.pucp.canchalibre.modelo.transaccion;

import pe.edu.pucp.canchalibre.modelo.cancha.Etiqueta;

import java.util.Date;
import java.util.List;

public class Comprobante{
	private int idComprobante;
	private double igv;
	//private double precioHora; el precio de la Hora está contenido en un EsquemaPrecio
	//que tiene una Cancha, que asu vez tiene una Reserva
	private Date fechaEmision;

	public Date getFechaEmision() {
		return fechaEmision;
	}
	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

//	public double getPrecioHora() {
//		return precioHora;
//	}
//
//	public void setPrecioHora(double precioHora) {
//		this.precioHora = precioHora;
//	}

	public double getIgv() {
		return igv;
	}
	public void setIgv(double igv) {
		this.igv = igv;
	}

	public int getIdComprobante() {
		return idComprobante;
	}
	public void setIdComprobante(int idComprobante) {
		this.idComprobante = idComprobante;
	}
}
