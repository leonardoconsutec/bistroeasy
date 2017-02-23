package br.com.consutec.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Conta;
import br.com.consutec.models.Mesa;

@Stateless
public class ContaDAO extends BaseDAO<Conta> {
	@PersistenceContext(unitName = "bistroPU")
	private EntityManager em;
	
	public ContaDAO(){
		super(Conta.class);
	}
	
	public Conta findbyMesa(Mesa mesa){
		StringBuilder sql = new StringBuilder();
		sql.append("from Conta where mesa = :ms and status = :st");
		Query query =  em.createQuery(sql.toString());
		query.setParameter("ms", mesa);
		query.setParameter("st", Long.valueOf("1"));
		return (Conta) query.getSingleResult();
	}
}
