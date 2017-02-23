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

import br.com.consutec.dao.EmpresaDAO;
import br.com.consutec.models.Empresa;
import br.com.consutec.models.Endereco;

@ManagedBean
@ViewScoped
public class EmpresaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8410422780693423590L;

	@EJB
	private EmpresaDAO empresaDAO;
	
	private Empresa empresa;
	private List<Empresa> empresas;

	@PostConstruct
	public void init() {
		empresa = new Empresa();
	}

	public String save() {
		try {
			empresaDAO.save(empresa);
			FacesMessageUtils.info("Empresa adicionado com sucesso!");
			return "listar";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar a Empresa");
			return null;
		}
	}

	public void update() {
		try {
			empresa = empresaDAO.update(empresa);
			FacesMessageUtils.info("Empresa foi atualizada com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel atualizar a Empresa");
		}
	}

	public void remove() {
		try {
			empresaDAO.remove(empresa);
			FacesMessageUtils.info("Empresa foi removida com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel remover a Empresa");
		}
	}

	public void buscarEnderecoCEP(){
		String cep = empresa.getCep().replaceAll("-", "");
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
        	empresa.setLogradouro(endereco.getTipo_logradouro()+" "+endereco.getLogradouro());
            empresa.setBairro(endereco.getBairro());
            empresa.setCidade(endereco.getCidade());
            empresa.setUf(endereco.getUf());	
        }else{
        	empresa.setLogradouro("");
            empresa.setBairro("");
            empresa.setCidade("");
            empresa.setUf("");
            FacesMessageUtils.error("Cep Informado não existe!");
        }
        
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Empresa> getEmpresas() {
		empresas = empresaDAO.listAll();
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

}
