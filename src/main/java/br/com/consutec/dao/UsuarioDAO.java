package br.com.consutec.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Usuario;
@Stateless
public class UsuarioDAO extends BaseDAO<Usuario>{
	@PersistenceContext(unitName="bistroPU")
    private EntityManager em;
	
	public UsuarioDAO(){
		super(Usuario.class);
	}

	public Usuario buscaUsuario(String login, String senha, boolean ativo) {
		StringBuilder sql = new StringBuilder();
		sql.append("from Usuario where upper(login) = :login and upper(senha) = :senha and ativo = :ativo");
		Query query = em.createQuery(sql.toString());
		query.setParameter("login", login.toUpperCase());
		query.setParameter("senha", senha.toUpperCase());
		query.setParameter("ativo", ativo);
		return (Usuario) query.getSingleResult();
	}
     
}
