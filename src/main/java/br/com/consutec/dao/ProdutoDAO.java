package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Produto;

@Stateless
public class ProdutoDAO extends BaseDAO<Produto> {
	public ProdutoDAO(){
		super(Produto.class);
	}
}
