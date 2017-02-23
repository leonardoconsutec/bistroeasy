package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Loja;
@Stateless
public class LojaDAO extends BaseDAO<Loja> {
	public LojaDAO(){
		super(Loja.class);
	}
}
