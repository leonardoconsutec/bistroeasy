package br.com.consutec.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Loja;
import br.com.consutec.models.Mesa;

@Stateless
public class MesaDAO extends BaseDAO<Mesa> {
	@PersistenceContext(unitName="bistroPU")
    private EntityManager em;
	
	public MesaDAO(){
		super(Mesa.class);
	}

	public List<Mesa> listarMesasLoja(Loja loja) {
		StringBuilder sql = new StringBuilder();
		sql.append("from Mesa where loja = :loja order by id");
		Query query =  em.createQuery(sql.toString());
		query.setParameter("loja", loja);
		return query.getResultList();
	}
	
	public Mesa findMesaLoja(Loja loja, Long seq) {
		StringBuilder sql = new StringBuilder();
		sql.append("from Mesa where loja = :loja and sequencial = :seq " );
		Query query =  em.createQuery(sql.toString());
		query.setParameter("loja", loja);
		query.setParameter("seq", seq);
		return (Mesa) query.getSingleResult();
	}
	
	public List<Mesa> listarMesasAbertas(Loja loja) {
		StringBuilder sql = new StringBuilder();
		sql.append("from Mesa where loja = :loja and status = :st" );
		Query query =  em.createQuery(sql.toString());
		query.setParameter("loja", loja);
		query.setParameter("st", Long.valueOf("1"));
		return query.getResultList();
	}
	
	public Long findMaxSeq(Loja loja){
		StringBuilder sql = new StringBuilder();
		sql.append("select max(sequencial) from Mesa where loja = :lj");
		Query query =  em.createQuery(sql.toString());
		query.setParameter("lj", loja);
		return (Long) query.getSingleResult();
	}
}
