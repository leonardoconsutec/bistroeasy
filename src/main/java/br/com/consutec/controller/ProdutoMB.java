package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
		inicializaProduto();
	}
	
	public void inicializaProduto(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("id");
		if(id != null){
		   produto = produtoDAO.findByid(Long.valueOf(id));
		}
	}
	
	public String editar(){
		return "atualizar.jsf?faces-redirect=true&id="+produto.getId();
	}
	
	
	public String save() {
		try {
			produtoDAO.save(produto);
			FacesMessageUtils.info("Produto adicionado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar o Produto");
			return null;
		}
	}

	public String update() {
		try {
			produto = produtoDAO.update(produto);
			FacesMessageUtils.info("produto atualizado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível atualizar o Produto");
			return null;
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
