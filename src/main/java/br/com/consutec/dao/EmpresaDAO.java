package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Empresa;
@Stateless
public class EmpresaDAO extends BaseDAO<Empresa>{
	public EmpresaDAO(){
		super(Empresa.class);
	}
}
