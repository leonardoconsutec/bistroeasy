package br.com.consutec.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.CaixaDAO;
import br.com.consutec.dao.ContaDAO;
import br.com.consutec.dao.MesaDAO;
import br.com.consutec.dao.OperadorDAO;
import br.com.consutec.models.Caixa;
import br.com.consutec.models.Conta;
import br.com.consutec.models.Operador;
import br.com.consutec.models.Pagamento;
import br.com.consutec.models.ResumoCaixa;
import br.com.consutec.util.relatoriosUtil;

@ManagedBean
@ViewScoped
public class CaixaMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6566618849497705022L;

	private Caixa caixa;
	private Operador operador;
	private List<Caixa> caixas;
	private ResumoCaixa resumo;
	
	@EJB
	private CaixaDAO caixaDAO;
	
	@EJB
	private MesaDAO mesaDAO;
	
	@EJB
	private ContaDAO contaDAO;
	
	@EJB
	private OperadorDAO operadorDAO;
	
	@Inject
	protected SessaoMB sessao;
	
	
	@PostConstruct
	private void init(){
		caixa = new Caixa();
		operador = new Operador();
		caixas = new ArrayList<>();
		gerarPosicaoCaixa();
	}
	
	public void gerarPosicaoCaixa(){
		try {
			resumo = new ResumoCaixa();
			resumo.setDinheiro(BigDecimal.ZERO);
			resumo.setCartao(BigDecimal.ZERO);
			resumo.setCheque(BigDecimal.ZERO);
			resumo.setConvenio(BigDecimal.ZERO);
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			
			for(Conta conta : caixa.getContas()){
                
				for(Pagamento pagamento : conta.getPagamentos()){
					if(pagamento.getTipoPagamento().equals(Long.valueOf("0"))){
						resumo.setDinheiro(resumo.getDinheiro().add(pagamento.getValor()));  
					}
					if(pagamento.getTipoPagamento().equals(Long.valueOf("1"))){
						resumo.setCartao(resumo.getCartao().add(pagamento.getValor()));
					}
					if(pagamento.getTipoPagamento().equals(Long.valueOf("2"))){
						resumo.setCheque(resumo.getCheque().add(pagamento.getValor()));
					}
					if(pagamento.getTipoPagamento().equals(Long.valueOf("3"))){
						resumo.setConvenio(resumo.getConvenio().add(pagamento.getValor()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void abrirCaixa(){
		try {
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			if(caixa == null){
				caixa = new Caixa();
				caixa.setLoja(sessao.getLojaSelecionada());
				operador = operadorDAO.findByid(operador.getId());
				caixa.setOperador(operador);
				caixa.setDataHoraAbertura(Calendar.getInstance());
				caixa.setStatus(Long.valueOf("1"));
				caixaDAO.save(caixa);
				FacesMessageUtils.info("Caixa aberto com sucesso!");
			}else{
				FacesMessageUtils.info("Caixa Já está aberto!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível abrir o caixa!");
		}
		
	}
	
	public void fecharCaixa(){
		try {
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			if(caixa == null){
				FacesMessageUtils.info("O Caixa não está aberto!");
			}else{
				if(mesaDAO.listarMesasAbertas(sessao.getLojaSelecionada()).isEmpty()){
					caixa.setStatus(Long.valueOf("0"));
					caixaDAO.update(caixa);
					resumo = new ResumoCaixa();
					resumo.setDinheiro(BigDecimal.ZERO);
					resumo.setCartao(BigDecimal.ZERO);
					resumo.setCheque(BigDecimal.ZERO);
					resumo.setConvenio(BigDecimal.ZERO);
					FacesMessageUtils.info("Caixa fechado com sucesso!");	
				}else{
					FacesMessageUtils.info("Existem mesas abertas! Favor fechar todas as mesas antes de fechar o caixa. ");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessageUtils.error("Não foi possível fechar o caixa!");
		}
	}
	
	public void imprimePosicao(){
		try {
			gerarPosicaoCaixa();
			List<ResumoCaixa> resumos = new ArrayList<>();
			resumos.add(resumo);
			HashMap parametros = new HashMap();
			relatoriosUtil.imprimeRelatorio("posicaocaixa", parametros, resumos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public List<Caixa> getCaixas() {
		return caixas;
	}

	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public ResumoCaixa getResumo() {
		return resumo;
	}

	public void setResumo(ResumoCaixa resumo) {
		this.resumo = resumo;
	}
	
	
}
