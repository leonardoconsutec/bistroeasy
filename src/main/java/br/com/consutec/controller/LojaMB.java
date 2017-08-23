package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.LojaDAO;
import br.com.consutec.models.Endereco;
import br.com.consutec.models.Loja;
@ManagedBean
@ViewScoped
public class LojaMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3147435378981754071L;
	private Loja loja;
	private List<Loja> lojas;
	
	@EJB
	private LojaDAO lojaDAO;
	
	@PostConstruct
	public void init(){
		loja = new Loja();
		inicializaLoja();
	}
	
	public void inicializaLoja(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("id");
		if(id != null){
		   loja = lojaDAO.findByid(Long.valueOf(id));
		}
	}
	
	public String editar(){
		return "atualizar.jsf?faces-redirect=true&id="+loja.getId();
	}

	public String save() {
		try {
			lojaDAO.save(loja);
			FacesMessageUtils.info("Loja adicionado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar a Loja");
			return null;
		}
	}

	public String update() {
		try {
			loja = lojaDAO.update(loja);
			FacesMessageUtils.info("Loja atualizada com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível atualizar a loja");
			return null;
		}
	}

	public void remove() {
		try {
			lojaDAO.remove(loja);
			FacesMessageUtils.info("Loja foi removida com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel remover a Loja");
		}
	}
	
	public void buscarEnderecoCEP(){
		String cep = loja.getCep().replaceAll("-", "");
		StringBuilder URL = new StringBuilder();
		URL.append("http://cep.republicavirtual.com.br/web_cep.php?cep=").append(cep).append("&formato=json");
		Endereco endereco = new Endereco();
		Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URL.toString());
        Response response = target.request().get();
        String json = response.readEntity(String.class);
        response.close();
        endereco = new Gson().fromJson(json, Endereco.class);
        Integer resp = Integer.parseInt(endereco.getResultado());
        if(resp > 0){
        	loja.setLogradouro(endereco.getTipo_logradouro()+" "+endereco.getLogradouro());
            loja.setBairro(endereco.getBairro());
            loja.setCidade(endereco.getCidade());
            loja.setUf(endereco.getUf());	
        }else{
        	loja.setLogradouro("");
            loja.setBairro("");
            loja.setCidade("");
            loja.setUf("");
            FacesMessageUtils.error("Cep Informado não existe!");
        }
        
	}
	
	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	public List<Loja> getLojas() {
		lojas = lojaDAO.listAll();
		return lojas;
	}

	public void setLojas(List<Loja> lojas) {
		this.lojas = lojas;
	}

	public LojaDAO getLojaDAO() {
		return lojaDAO;
	}

	public void setLojaDAO(LojaDAO lojaDAO) {
		this.lojaDAO = lojaDAO;
	}
	
	

}
