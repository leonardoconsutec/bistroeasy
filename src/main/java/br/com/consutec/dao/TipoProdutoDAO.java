package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.TipoProduto;

@Stateless
public class TipoProdutoDAO extends BaseDAO<TipoProduto> {
	public TipoProdutoDAO(){
		super(TipoProduto.class);
	}
}
