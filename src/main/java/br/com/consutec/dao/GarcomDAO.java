package br.com.consutec.dao;

import javax.ejb.Stateless;

import br.com.consutec.models.Garcom;

@Stateless
public class GarcomDAO extends BaseDAO<Garcom> {
	public GarcomDAO(){
		super(Garcom.class);
	}
}
