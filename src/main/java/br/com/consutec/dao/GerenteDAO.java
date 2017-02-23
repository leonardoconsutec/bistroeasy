package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Gerente;

@Stateless
public class GerenteDAO extends BaseDAO<Gerente> {
	public GerenteDAO(){
		super(Gerente.class);
	}
}
