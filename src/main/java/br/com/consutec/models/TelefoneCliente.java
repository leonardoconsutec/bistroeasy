package br.com.consutec.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="telefonecliente")
public class TelefoneCliente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3039572407453248936L;
	@Id
    private Long id;
	@Column(name="numero",length=15)
	private String numero;
	@OneToOne
	private Cliente cliente;
	@OneToOne
    @JoinColumn(name="operadoraTelefone")
	private OperadoraTelefone operadoraTelefone;
	@Column(name="ativo")
	private boolean ativo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public OperadoraTelefone getOperadoraTelefone() {
		return operadoraTelefone;
	}
	public void setOperadoraTelefone(OperadoraTelefone operadoraTelefone) {
		this.operadoraTelefone = operadoraTelefone;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TelefoneCliente other = (TelefoneCliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
		
}
