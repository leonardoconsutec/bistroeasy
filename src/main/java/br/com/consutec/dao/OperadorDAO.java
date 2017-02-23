package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Operador;
@Stateless
public class OperadorDAO extends BaseDAO<Operador>{
	public OperadorDAO(){
		super(Operador.class);
	}
}
