package br.com.consutec.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseDAO<T> {
     @PersistenceContext(unitName="bistroPU")
     private EntityManager em;
     
     public Class<T> tipo;
     
     public BaseDAO(){}

     public BaseDAO(Class<T> tipo){
    	 this.tipo = tipo;
     }
     public void save(T tipo){
    	 em.persist(tipo);
     }
     public T update(T tipo){
    	 return em.merge(tipo);
     }
	public List<T> listAll(){
    	 StringBuilder sql = new StringBuilder();
    	 sql.append("from ").append(tipo.getSimpleName());
    	 Query q = em.createQuery(sql.toString());
    	 return q.getResultList();
     }
     public T findByid(Long id){
    	 return em.find(tipo, id);
     }
     public void remove(T tipo){
    	 tipo = em.merge(tipo);
    	 em.remove(tipo);
     }
     
}
