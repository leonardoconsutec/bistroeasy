package br.com.consutec.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.consutec.models.Conta;
import br.com.consutec.models.ItemConta;

@Stateless
public class ItemContaDAO extends BaseDAO<ItemConta> {
	public ItemContaDAO() {
		super(ItemConta.class);
	}

	@PersistenceContext(unitName = "bistroPU")
	private EntityManager em;

	public List<ItemConta> listarPorConta(Conta conta) {
		StringBuilder sql = new StringBuilder();
		sql.append("from ItemConta where conta = :ct order by produto");
		Query query = em.createQuery(sql.toString());
		query.setParameter("ct", conta);
		return query.getResultList();
	}

	public List<ItemConta> listarImpressao(Conta conta) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ic.produto as id"
				+ "      , ic.produto as produto"
				+ "      , ic.conta as conta"
				+ "      , SUM(ic.quantidade) as quantidade"
				+ "      , AVG(ic.valorunitario) valorUnitario "
				+ " FROM   itemconta ic "
				+ " INNER  JOIN produto prd ON (prd.id = ic.produto) "
				+ " WHERE  ic.conta = :ct "
				+ " GROUP BY ic.produto, prd.descricao, ic.conta"
				+ " ORDER BY ic.produto ");
		Query query =  em.createNativeQuery(sql.toString(), ItemConta.class);
		query.setParameter("ct", conta.getId());
		return query.getResultList();
	}
}
