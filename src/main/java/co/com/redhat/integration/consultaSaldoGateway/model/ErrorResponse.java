package co.com.redhat.integration.consultaSaldoGateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
	
	@JsonProperty(value="codigo")
	private String codigo;
	@JsonProperty(value="descripcion")
	private String descripcion;
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
