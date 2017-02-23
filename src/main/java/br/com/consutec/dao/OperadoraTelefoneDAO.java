package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.OperadoraTelefone;
@Stateless
public class OperadoraTelefoneDAO extends BaseDAO<OperadoraTelefone>{
	public OperadoraTelefoneDAO(){
		super(OperadoraTelefone.class);
	}
}
