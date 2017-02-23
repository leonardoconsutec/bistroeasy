package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Sistema;

@Stateless
public class SistemaDAO extends BaseDAO<Sistema>{
	public SistemaDAO() {
		super(Sistema.class);
	}

}
