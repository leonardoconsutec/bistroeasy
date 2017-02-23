package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.UsuarioDAO;
import br.com.consutec.models.Usuario;

@ManagedBean
@ViewScoped
public class UsuarioMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4198786644079687807L;

	@EJB
	private UsuarioDAO usuarioDAO;

	private Usuario usuario;
	private List<Usuario> usuarios;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	public String save() {
		try {
			usuarioDAO.save(usuario);
			FacesMessageUtils.info("Usuario adicionado com sucesso!");
            return "listar";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel adicionar o usuario");
			return null;
		}
		
	}

	public void update() {
		try {
			usuario = usuarioDAO.update(usuario);
			FacesMessageUtils.info("Usuario atualizado com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel atualizar o usuario");
		}
		
	}

	public void remove() {
		try {
			usuarioDAO.remove(usuario);
			FacesMessageUtils.info("Usuario removido com sucesso!");
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não Foi possivel remover o usuario");
		}

	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		usuarios = usuarioDAO.listAll();
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

}
