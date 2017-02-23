package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.TipoProdutoDAO;
import br.com.consutec.models.TipoProduto;

@ManagedBean
@ViewScoped
public class TipoProdutoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5352320700881813355L;
	
	@EJB
	private TipoProdutoDAO tipoProdutoDAO;
	
	private TipoProduto tipoProduto;
	private List<TipoProduto> tiposProduto;
	
	@PostConstruct
	public void init(){
		tipoProduto = new TipoProduto();
	}

	public String save(){
		try {
			tipoProdutoDAO.save(tipoProduto);
			FacesMessageUtils.info("Tipo adicionado com sucesso!");
			return "listar";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar a Tipo");
			return null;
		}
	}
	public void update(){
		try {
			tipoProdutoDAO.update(tipoProduto);
			FacesMessageUtils.info("Tipo atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel atualizar o Tipo");
		}
		
	}
	public void remove(){
		try {
			tipoProdutoDAO.remove(tipoProduto);
			FacesMessageUtils.info("Tipo removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel remover o Tipo");
		}
		
	}
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public List<TipoProduto> getTiposProduto() {
		tiposProduto = tipoProdutoDAO.listAll();
		return tiposProduto;
	}

	public void setTiposProduto(List<TipoProduto> tiposProduto) {
		this.tiposProduto = tiposProduto;
	}
	
	
	
}
