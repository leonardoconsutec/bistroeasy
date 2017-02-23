package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Cliente;
@Stateless
public class ClienteDAO extends BaseDAO<Cliente>{
	public ClienteDAO(){
		super(Cliente.class);
	}
}
