package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.OperadorDAO;
import br.com.consutec.models.Endereco;
import br.com.consutec.models.Operador;

@ManagedBean
@ViewScoped
public class OperadorMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -331914364391829275L;
	private Operador operador;
	private List<Operador> operadores;
	private List<Operador> operadoresFiltrados;
	
	@EJB
	private OperadorDAO operadorDAO;
	
	@PostConstruct
	private void init(){
		operador = new Operador();
	}

	public String save() {
		try {
			operadorDAO.save(operador);
			FacesMessageUtils.info("Operador adicionado com sucesso!");
			return "listar";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível adicionar o Operador");
			return null;
		}
	}

	public void update() {
		try {
			operador = operadorDAO.update(operador);
			FacesMessageUtils.info("Operador Atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel atualizar o operador!");
		}
	}

	public void remove() {
		try {
			operadorDAO.remove(operador);
			FacesMessageUtils.info("Operador removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possivel salvar o operador!");
		}
	}

	public void buscarEnderecoCEP(){
		String cep = operador.getCep().replaceAll("-", "");
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
        	operador.setLogradouro(endereco.getTipo_logradouro()+" "+endereco.getLogradouro());
            operador.setBairro(endereco.getBairro());
            operador.setCidade(endereco.getCidade());
            operador.setUf(endereco.getUf());	
        }else{
        	operador.setLogradouro("");
            operador.setBairro("");
            operador.setCidade("");
            operador.setUf("");
			FacesMessageUtils.error("Cep informado não foi encontrado!");
        }
        
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public List<Operador> getOperadores() {
		operadores = operadorDAO.listAll();
		return operadores;
	}

	public void setOperadores(List<Operador> operadores) {
		this.operadores = operadores;
	}

	public List<Operador> getOperadoresFiltrados() {
		return operadoresFiltrados;
	}

	public void setOperadoresFiltrados(List<Operador> operadoresFiltrados) {
		this.operadoresFiltrados = operadoresFiltrados;
	}
	
	

}
