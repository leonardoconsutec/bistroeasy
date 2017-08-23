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

import br.com.consutec.dao.GarcomDAO;
import br.com.consutec.models.Endereco;
import br.com.consutec.models.Garcom;

@ManagedBean
@ViewScoped
public class GarcomMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5692597759862720626L;
	private Garcom garcom;
	private List<Garcom> garcons;
	
	@EJB
	private GarcomDAO garcomDAO;
	
	@PostConstruct
	public void init(){
		garcom = new Garcom();
		inicializaGarcom();
	}
	
	public void inicializaGarcom(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("id");
		if(id != null){
		   garcom = garcomDAO.findByid(Long.valueOf(id));
		}
	}
	
	public String editar(){
		return "atualizar.jsf?faces-redirect=true&id="+garcom.getId();
	}
	
	public String save() {
		try {
			garcomDAO.save(garcom);
			FacesMessageUtils.info("Garçon adicionado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível adicionar o Garçon");
			return null;
		}
	}

	public String update() {
		try {
			garcom = garcomDAO.update(garcom);
			FacesMessageUtils.info("Garçon Atualizado com sucesso!");
			return "listar.jsf?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel atualizar o garçon!");
			return null;
		}
	}

	public void remove() {
		try {
			garcomDAO.remove(garcom);
			FacesMessageUtils.info("Garçon removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel salvar o garçon!");
			
		}
	}

	public void buscarEnderecoCEP(){
		String cep = garcom.getCep().replaceAll("-", "");
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
        	garcom.setLogradouro(endereco.getTipo_logradouro()+" "+endereco.getLogradouro());
            garcom.setBairro(endereco.getBairro());
            garcom.setCidade(endereco.getCidade());
            garcom.setUf(endereco.getUf());	
        }else{
        	garcom.setLogradouro("");
            garcom.setBairro("");
            garcom.setCidade("");
            garcom.setUf("");
			FacesMessageUtils.error("Cep informado não foi encontrado!");
        }
        
	}
	
	public Garcom getGarcom() {
		return garcom;
	}

	public void setGarcom(Garcom garcom) {
		this.garcom = garcom;
	}

	public List<Garcom> getGarcons() {
		garcons = garcomDAO.listAll();
		return garcons;
	}

	public void setGarcons(List<Garcom> garcons) {
		this.garcons = garcons;
	}

	
}
