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

import br.com.consutec.dao.GerenteDAO;
import br.com.consutec.models.Endereco;
import br.com.consutec.models.Gerente;

@ManagedBean
@ViewScoped
public class GerenteMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8867241631729396664L;
	private Gerente gerente;
	private List<Gerente> gerentes;
	@EJB
	private GerenteDAO gerenteDAO;
	
	@PostConstruct
	private void init(){
		gerente = new Gerente();
		inicializaGerente();
	}
	
	public void inicializaGerente(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("id");
		if(id != null){
		   gerente = gerenteDAO.findByid(Long.valueOf(id));
		}
	}
	
	public String editar(){
		return "atualizar.jsf?faces-redirect=true&id="+gerente.getId();
	}
	
	public String save() {
		try {
			gerenteDAO.save(gerente);
			FacesMessageUtils.info("Gerente adicionado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível adicionar o Gerente");
			return null;
		}
	}

	public String update() {
		try {
			gerente = gerenteDAO.update(gerente);
			FacesMessageUtils.info("Gerente Atualizado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel atualizar o gerente!");
			return null;
		}
	}

	public void remove() {
		try {
			gerenteDAO.remove(gerente);
			FacesMessageUtils.info("Gerente removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel salvar o Gerente!");
		}
	}

	public void buscarEnderecoCEP(){
		String cep = gerente.getCep().replaceAll("-", "");
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
        	gerente.setLogradouro(endereco.getTipo_logradouro()+" "+endereco.getLogradouro());
            gerente.setBairro(endereco.getBairro());
            gerente.setCidade(endereco.getCidade());
            gerente.setUf(endereco.getUf());	
        }else{
        	gerente.setLogradouro("");
            gerente.setBairro("");
            gerente.setCidade("");
            gerente.setUf("");
			FacesMessageUtils.error("Cep informado não foi encontrado!");
        }
        
	}

	public Gerente getGerente() {
		return gerente;
	}

	public void setGerente(Gerente gerente) {
		this.gerente = gerente;
	}

	public List<Gerente> getGerentes() {
		gerentes = gerenteDAO.listAll();
		return gerentes;
	}

	public void setGerentes(List<Gerente> gerentes) {
		this.gerentes = gerentes;
	}
	

}
