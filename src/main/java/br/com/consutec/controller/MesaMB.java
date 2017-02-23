package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.MesaDAO;
import br.com.consutec.models.Mesa;

@ManagedBean
@ViewScoped
public class MesaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8956028519417809511L;
	private Mesa mesa;
	private List<Mesa> mesas;
	private Long qtdMesas;
	
	@Inject
	protected SessaoMB sessao;

	@EJB
	private MesaDAO mesaDAO;

	@PostConstruct
	private void init() {
		mesa = new Mesa();
	}

	public String save() {
		try {
			Long ultimoId = mesaDAO.findMaxSeq(sessao.getLojaSelecionada());
			for (int i = 1; i <= qtdMesas; i++) {
				System.out.println(i);
				mesa = new Mesa();
				if (ultimoId == null) {
					mesa.setLoja(sessao.getLojaSelecionada());
					mesa.setSequencial(Long.valueOf(i));
					mesa.setStatus(Long.valueOf("0"));
					mesaDAO.save(mesa);
				} else {
					Long sequencial = Long.valueOf("0");
					mesa.setLoja(sessao.getLojaSelecionada());
					sequencial = ultimoId + i;
					mesa.setSequencial(Long.valueOf(sequencial));
					mesa.setStatus(Long.valueOf("0"));
					mesaDAO.save(mesa);
				}
			}
			FacesMessageUtils.info("Mesas cadastradas com sucesso!");
			return "listar";
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível cadastrar as mesas!");
			return null;
		}

	}
	
	public void remove(){
		try {
			Long ultimoId = mesaDAO.findMaxSeq(sessao.getLojaSelecionada());
			if(ultimoId != mesa.getSequencial()){
				FacesMessageUtils.error("Somente é permitido remover a ultima mesa cadastrada, caso essa mesa não tenha movimento");
			}else{
				mesaDAO.remove(mesa);
				FacesMessageUtils.info("Mesa removida com sucesso!");
			}
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível remover a mesa!");
		}
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	public List<Mesa> getMesas() {
		mesas = mesaDAO.listarMesasLoja(sessao.getLojaSelecionada());
		return mesas;
	}

	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public Long getQtdMesas() {
		return qtdMesas;
	}

	public void setQtdMesas(Long qtdMesas) {
		this.qtdMesas = qtdMesas;
	}

}
