package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.TelefoneCliente;
@Stateless
public class TelefoneClienteDAO extends BaseDAO<TelefoneCliente> {
	public TelefoneClienteDAO(){
		super(TelefoneCliente.class);
	}
}
