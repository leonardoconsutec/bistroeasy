package br.com.consutec.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Caixa;
import br.com.consutec.models.Loja;

@Stateless
public class CaixaDAO extends BaseDAO<Caixa> {

	@PersistenceContext(unitName = "bistroPU")
	private EntityManager em;

	public CaixaDAO(){
		super(Caixa.class);
	}
	
	public Caixa findbyLoja(Loja loja) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("from Caixa where loja = :lj and status = :st ");
			Query q = em.createQuery(sql.toString());
			q.setParameter("lj", loja);
			q.setParameter("st", Long.valueOf("1"));
			return (Caixa) q.getSingleResult();	
		} catch (NoResultException e) {
			return null;
		}
		
	}

}
