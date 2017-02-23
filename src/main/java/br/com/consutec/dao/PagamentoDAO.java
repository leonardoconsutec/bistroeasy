package br.com.consutec.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Conta;
import br.com.consutec.models.Pagamento;
@Stateless
public class PagamentoDAO extends BaseDAO<Pagamento>{
	public PagamentoDAO(){
		super(Pagamento.class);
	}
	
	@PersistenceContext(unitName="bistroPU")
    private EntityManager em;
	
	public List<Pagamento> listarPorConta(Conta conta) {
		StringBuilder sql = new StringBuilder();
		sql.append("from Pagamento where conta = :ct order by id");
		Query query =  em.createQuery(sql.toString());
		query.setParameter("ct", conta);
		return query.getResultList();
	}
}

