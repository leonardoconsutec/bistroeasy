package br.com.consutec.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.CaixaDAO;
import br.com.consutec.dao.ContaDAO;
import br.com.consutec.dao.GarcomDAO;
import br.com.consutec.dao.ItemContaDAO;
import br.com.consutec.dao.MesaDAO;
import br.com.consutec.dao.PagamentoDAO;
import br.com.consutec.dao.ProdutoDAO;
import br.com.consutec.models.Caixa;
import br.com.consutec.models.Conta;
import br.com.consutec.models.Garcom;
import br.com.consutec.models.ItemConta;
import br.com.consutec.models.Loja;
import br.com.consutec.models.Mesa;
import br.com.consutec.models.Pagamento;
import br.com.consutec.models.Produto;
import br.com.consutec.models.ResumoCaixa;
import br.com.consutec.util.relatoriosUtil;

@ManagedBean
@ViewScoped
public class ContaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9210910980077635773L;

	private Mesa mesa;
	private Conta conta;
	private Produto produto;
	private Caixa caixa;
	private Garcom garcom;
	private ItemConta itemConta;
	private BigDecimal idprd;
	private String descricaoProd = "";
	private BigDecimal mesaTroca;
	private Pagamento pagamento;
	private BigDecimal totalPago;
	private BigDecimal troco;

	private List<Produto> produtos;
	private List<Produto> produtosFiltrados;
	private List<ItemConta> itensConta;
	private List<Garcom> garcons;
	private List<Garcom> garconsFiltrados;
	private List<Pagamento> pagamentos;

	@Inject
	protected SessaoMB sessao;

	@EJB
	private CaixaDAO caixaDAO;

	@EJB
	private MesaDAO mesaDAO;

	@EJB
	private ContaDAO contaDAO;

	@EJB
	private ProdutoDAO produtoDAO;

	@EJB
	private ItemContaDAO itemContaDAO;

	@EJB
	private GarcomDAO garcomDAO;

	@EJB
	private PagamentoDAO pagamentoDAO;

	@PostConstruct
	private void init() {
		mesa = new Mesa();
		conta = new Conta();
		garcom = new Garcom();
		caixa = new Caixa();
		itemConta = new ItemConta();
		itemConta.setQuantidade(BigDecimal.ONE);
		pagamento = new Pagamento();
		produto = new Produto();
		totalPago = BigDecimal.ZERO;
		troco = BigDecimal.ZERO;
	}

	public String verificaMesa() {
		try {
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			if (caixa == null) {
				FacesMessageUtils.info("O caixa não está aberto!");
				return null;
			} else {
				Long seq = mesa.getSequencial();
				mesa = mesaDAO.findMesaLoja(sessao.getLojaSelecionada(), seq);
				if (mesa.getStatus() == 0) {
					RequestContext.getCurrentInstance().execute("PF('dialogAbrirMesa').show();");
					return null;
				} else {
					return "detalhes?id="+mesa.getId()+"&faces-redirect=true";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "detalhes?id="+mesa.getId()+"&faces-redirect=true";
		}
	}
	
	public void inicializaMesa(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String id = request.getParameter("id");
		mesa = mesaDAO.findByid(Long.valueOf(id));
		conta = contaDAO.findbyMesa(mesa);
		itensConta = itemContaDAO.listarPorConta(conta);
		pagamentos = pagamentoDAO.listarPorConta(conta);
		conta.setItens(itensConta);
		conta.setPagamentos(pagamentos);
		calculaTotalPago();
		calculaTroco();
	}

	public String verificaMesa(String id) {
		try {
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			if (caixa == null) {
				FacesMessageUtils.info("O caixa não está aberto!");
				return null;
			} else {
				Long idenviado = Long.valueOf(id);
				mesa = mesaDAO.findByid(idenviado);
				if (mesa.getStatus() == 0) {
					RequestContext.getCurrentInstance().execute("PF('dialogAbrirMesa').show();");
					return null;
				} else {
					//RequestContext.getCurrentInstance().execute("PF('detalhes').show();");
					return "detalhes?id="+mesa.getId()+"&faces-redirect=true";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String abrirMesa() {
		try {
			RequestContext.getCurrentInstance().execute("PF('dialogAbrirMesa').hide();");
			conta = new Conta();
			garcom = garcomDAO.findByid(garcom.getId());
			caixa = caixaDAO.findbyLoja(sessao.getLojaSelecionada());
			mesa = mesaDAO.findByid(mesa.getId());
			mesa.setStatus(Long.valueOf("1"));
			conta.setMesa(mesa);
			conta.setCaixa(caixa);
			conta.setGarcon(garcom);
			conta.setStatus(Long.valueOf("1"));
			conta.setValorTotal(BigDecimal.ZERO);
			conta.setTotalProdutos(BigDecimal.ZERO);
			conta.setTaxaServico(BigDecimal.ZERO);
			conta.setDesconto(BigDecimal.ZERO);
			conta.setPagamentos(new ArrayList<>());
			conta.setItens(new ArrayList<>());
			conta.setLoja(sessao.getLojaSelecionada());
			contaDAO.save(conta);
			mesaDAO.update(mesa);
			atualizarListaMesas();
			//RequestContext.getCurrentInstance().execute("PF('detalhes').show();");
			return "detalhes?id="+mesa.getId()+"&faces-redirect=true";
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível Abrir a mesa");
			return null;
		}

	}

	public void atualizarListaMesas() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.getFlash().setKeepMessages(true);
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String trocarMesa() {
		try {
			Mesa mesaAtual = mesa;
			Mesa mesaNova = mesaDAO.findMesaLoja(sessao.getLojaSelecionada(), mesaTroca.longValue());
			if (mesaNova.getStatus().equals(Long.valueOf("1"))) {
				FacesMessageUtils.info("Mesa Destino está ocupada!");
			} else {
				mesaAtual.setStatus(Long.valueOf("0"));
				mesaNova.setStatus(Long.valueOf("1"));
				mesaDAO.update(mesaAtual);
				mesaDAO.update(mesaNova);
				conta.setMesa(mesaNova);
				contaDAO.update(conta);
				FacesContext fc = FacesContext.getCurrentInstance();
				FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Mesa Trocada com sucesso!","Mesa Trocada com sucesso!");
				fc.getExternalContext().getFlash().setKeepMessages(true);
				fc.addMessage("", fm);
			}
			return "listar?faces-redirect=true";
			//atualizarListaMesas();
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível Fechar a mesa");
			e.printStackTrace();
			return null;
		}
	}

	public void adicionarPagamento() {
		try {
			pagamento.setConta(conta);
			pagamentoDAO.save(pagamento);
			pagamentos = pagamentoDAO.listarPorConta(conta);
			pagamento = new Pagamento();
			calculaTotalPago();
			calculaTroco();
			FacesMessageUtils.info("Pagamento Adicionado!");
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível adicionar o pagamento");
			e.printStackTrace();
		}
	}

	public void removerPagamento() {
		try {
			pagamentoDAO.remove(pagamento);
			pagamentos = pagamentoDAO.listarPorConta(conta);
			pagamento = new Pagamento();
			calculaTotalPago();
			calculaTroco();
			FacesMessageUtils.info("Pagamento Removido!");
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível remover o pagamento");
			e.printStackTrace();
		}
	}

	public void calculaTotalPago() {
		try {
			totalPago = BigDecimal.ZERO;
			for (Pagamento pg : pagamentos) {
				totalPago = totalPago.add(pg.getValor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void calculaTroco() {
		try {
			troco = BigDecimal.ZERO;
			if (totalPago.compareTo(conta.getValorTotal()) > 0) {
				troco = conta.getValorTotal().subtract(totalPago);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void fecharMesa() {
		try {
			if (conta.getValorTotal().compareTo(totalPago) <= 0) {
				mesa.setStatus(Long.valueOf("0"));
				mesaDAO.update(mesa);
				if (troco.compareTo(BigDecimal.ZERO) < 0) {
					Pagamento pgTroco = new Pagamento();
					pgTroco.setConta(conta);
					pgTroco.setTipoPagamento(Long.valueOf("0"));
					pgTroco.setValor(troco);
					pagamentoDAO.save(pgTroco);
				}
				conta.setStatus(Long.valueOf("0"));
				conta.setMesa(mesa);
				contaDAO.update(conta);
				atualizarListaMesas();
				FacesMessageUtils.info("Mesa Fechada com sucesso!");
			} else {
				RequestContext.getCurrentInstance().execute("PF('fechaConta').show();");
				FacesMessageUtils.info("Total de pagamentos não é suficiente para quitar a conta!");
			}
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível Fechar a mesa");
		}
	}

	public void encerrarConta() {
		try {
			mesa = mesaDAO.findByid(mesa.getId());
			mesa.setStatus(Long.valueOf("2"));
			mesaDAO.update(mesa);
			atualizarListaMesas();
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível encerrar a mesa!");
			e.printStackTrace();
		}

	}

	public void aplicarDesconto() {
		try {
			BigDecimal descontoInformado = BigDecimal.ZERO;
			BigDecimal descontoMax = BigDecimal.ZERO;
			BigDecimal totalProdutos = BigDecimal.ZERO;
			BigDecimal taxaServico = BigDecimal.ZERO;
			BigDecimal totalConta = BigDecimal.ZERO;
			if (conta.getDesconto().compareTo(BigDecimal.ZERO) >= 0) {

				descontoInformado = conta.getDesconto();
				totalProdutos = conta.getTotalProdutos();
				if (sessao.getLojaSelecionada().isUsaTaxaServicos()) {
					taxaServico = conta.getTaxaServico();
				}
				totalConta = totalProdutos.add(taxaServico);
				descontoMax = sessao.getLojaSelecionada().getDescontoMaximo();
				descontoMax = totalConta.multiply(descontoMax).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_DOWN);
				if (descontoInformado.compareTo(descontoMax) > 0) {
					FacesMessageUtils.info("Desconto informado é maior que o Maximo Permitido!");
				} else {
					totalConta = totalConta.subtract(descontoInformado);
					conta.setValorTotal(totalConta);
					conta.setDesconto(descontoInformado);
					contaDAO.update(conta);
					calculaTotalPago();
					calculaTroco();
					FacesMessageUtils.info("Desconto Aplicado");
				}
			} else {
				FacesMessageUtils.info("Desconto informado deve ser maior ou igual zero!");
			}
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível aplicar o desconto");
		}
	}

	public void buscarProduto(){
		try {
			produto = produtoDAO.findByid(idprd.longValue());
			descricaoProd = produto.getDescricao();
		} catch (Exception e) {
			descricaoProd = "Nenhum produto encontrado";
			FacesMessageUtils.error("produto não encontrado");
		}
	}
	
	public void selecaoFrmProduto(){
		idprd = new BigDecimal(produto.getId());
		descricaoProd = produto.getDescricao();
	}
	
	public String adicionarItemConta() {
		try {
			produto = produtoDAO.findByid(idprd.longValue());
			itemConta.setConta(conta);
			itemConta.setProduto(produto);
			itemConta.setValorUnitario(produto.getPrecoUnitario());
			conta.setTotalProdutos(
					conta.getTotalProdutos().add(itemConta.getValorUnitario().multiply(itemConta.getQuantidade())));
			if (sessao.getLojaSelecionada().isUsaTaxaServicos()) {
				Loja lj = sessao.getLojaSelecionada();
				BigDecimal totalprodutos = conta.getTotalProdutos();
				BigDecimal taxaServico = totalprodutos.multiply(lj.getTaxaServicos().divide(new BigDecimal(100)))
						.setScale(2, RoundingMode.HALF_UP);
				conta.setTaxaServico(taxaServico);
				conta.setValorTotal(totalprodutos.add(taxaServico));
			} else {
				conta.setTaxaServico(BigDecimal.ZERO);
				conta.setValorTotal(conta.getTotalProdutos());
			}
			itensConta = itemContaDAO.listarPorConta(conta);
			conta.setItens(itensConta);
			conta.getItens().add(itemConta);
			contaDAO.update(conta);
			itemConta = new ItemConta();
			itemConta.setQuantidade(BigDecimal.ONE);
			itensConta = itemContaDAO.listarPorConta(conta);
			idprd=null;
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Item adicionado com sucesso!","Item adicionado com sucesso!");
			fc.addMessage("",fm);
			fc.getExternalContext().getFlash().setKeepMessages(true);
			return "detalhes.jsf?id="+conta.getMesa().getId()+"&faces-redirect=true";
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível adicionar o item!");
			e.printStackTrace();
			return null;
		}
	}

	public String removerItemConta() {
		try {
			System.out.println("Teste");
			BigDecimal valorRemovido = BigDecimal.ZERO;
			BigDecimal valorProdutos = BigDecimal.ZERO;
			BigDecimal taxaServicos = BigDecimal.ZERO;
			valorRemovido = itemConta.getQuantidade().multiply(itemConta.getValorUnitario());
			valorProdutos = conta.getTotalProdutos();
			valorProdutos = valorProdutos.subtract(valorRemovido);
			if (sessao.getLojaSelecionada().isUsaTaxaServicos()) {
				Loja lj = sessao.getLojaSelecionada();
				taxaServicos = valorProdutos.multiply(lj.getTaxaServicos()).divide(new BigDecimal(100)).setScale(2,
						RoundingMode.HALF_UP);
			}
			conta.setTotalProdutos(valorProdutos);
			conta.setTaxaServico(taxaServicos);
			conta.setValorTotal(valorProdutos.add(taxaServicos));
			itensConta = itemContaDAO.listarPorConta(conta);
			conta.setItens(itensConta);
			conta.getItens().remove(itemConta);
			contaDAO.update(conta);
			FacesContext fc = FacesContext.getCurrentInstance();
			FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO,"Removido com sucesso!","Removido com sucesso!");
			fc.addMessage("",fm);
			fc.getExternalContext().getFlash().setKeepMessages(true);
			return "detalhes.jsf?id="+conta.getMesa().getId()+"&faces-redirect=true";
		} catch (Exception e) {
			FacesMessageUtils.error("Não foi possível remover o item!");
			e.printStackTrace();
			return null;
		}

	}
	
	public void imprimeConta(){
		try {
			Conta contaImpressao = conta;
			contaImpressao.setItens(itemContaDAO.listarImpressao(conta));
			List<Conta> ct = new ArrayList<>();
			ct.add(contaImpressao);
			HashMap parametros = new HashMap();
			relatoriosUtil.imprimeRelatorio("conta", parametros, ct);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Produto> getProdutos() {
		produtos = produtoDAO.listAll();
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public void setProdutosFiltrados(List<Produto> produtosFiltrados) {
		this.produtosFiltrados = produtosFiltrados;
	}

	public List<ItemConta> getItensConta() {

		return itensConta;
	}

	public void setItensConta(List<ItemConta> itensConta) {
		this.itensConta = itensConta;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Garcom getGarcom() {
		return garcom;
	}

	public void setGarcom(Garcom garcom) {
		this.garcom = garcom;
	}

	public List<Garcom> getGarcons() {
		garcons = garcomDAO.listAll();
		return garcons;
	}

	public void setGarcons(List<Garcom> garcons) {
		this.garcons = garcons;
	}

	public List<Garcom> getGarconsFiltrados() {
		return garconsFiltrados;
	}

	public void setGarconsFiltrados(List<Garcom> garconsFiltrados) {
		this.garconsFiltrados = garconsFiltrados;
	}

	public ItemConta getItemConta() {
		return itemConta;
	}

	public void setItemConta(ItemConta itemConta) {
		this.itemConta = itemConta;
	}

	public BigDecimal getIdprd() {
		return idprd;
	}

	public void setIdprd(BigDecimal idprd) {
		this.idprd = idprd;
	}

	public BigDecimal getMesaTroca() {
		return mesaTroca;
	}

	public void setMesaTroca(BigDecimal mesaTroca) {
		this.mesaTroca = mesaTroca;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}

	public BigDecimal getTroco() {
		return troco;
	}

	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}

	public String getDescricaoProd() {
		return descricaoProd;
	}

	public void setDescricaoProd(String descricaoProd) {
		this.descricaoProd = descricaoProd;
	}

}
