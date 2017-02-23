package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.ProdutoDAO;
import br.com.consutec.models.Produto;

@ManagedBean
@ViewScoped
public class ProdutoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2715898476642641356L;
	private Produto produto;
	private List<Produto> produtos;
	
	@EJB
	private ProdutoDAO produtoDAO;
	
	@PostConstruct
	private void init(){
		produto = new Produto();
	}
	
	public String save() {
		try {
			produtoDAO.save(produto);
			FacesMessageUtils.info("Produto adicionado com sucesso!");
			return "listar";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar o Produto");
			return null;
		}
	}

	public void update() {
		try {
			produto = produtoDAO.update(produto);
			FacesMessageUtils.info("produto atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível atualizar o Produto");
		}
	}

	public void remove() {
		try {
			produtoDAO.remove(produto);
			FacesMessageUtils.info("Produto foi removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel remover o Produto");
		}
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		produtos = produtoDAO.listAll();
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
}
