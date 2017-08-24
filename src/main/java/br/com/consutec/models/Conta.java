package br.com.consutec.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name="conta")
public class Conta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3512299197424920774L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "garcon")
    private Garcom garcon;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "mesa")
    private Mesa mesa;
    @Column(name = "totalprodutos")
    private BigDecimal totalProdutos;
    @Column(name = "taxaservico")
    private BigDecimal taxaServico;
    @Column(name = "desconto")
    private BigDecimal desconto;
    @Column(name = "valortotal")
    private BigDecimal valorTotal;
    @Column(name = "status")
    private Long status;
    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private List<ItemConta> itens;
    @OneToMany(mappedBy = "conta", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST}, orphanRemoval = true)
    private List<Pagamento> pagamentos;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="caixa")
    private Caixa caixa;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "loja")
    private Loja loja;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Garcom getGarcon() {
		return garcon;
	}
	public void setGarcon(Garcom garcon) {
		this.garcon = garcon;
	}
	public Mesa getMesa() {
		return mesa;
	}
	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public List<ItemConta> getItens() {
		return itens;
	}
	public void setItens(List<ItemConta> itens) {
		this.itens = itens;
	}
	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}
	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	public Caixa getCaixa() {
		return caixa;
	}
	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}
	
	public BigDecimal getTotalProdutos() {
		return totalProdutos;
	}
	public void setTotalProdutos(BigDecimal totalProdutos) {
		this.totalProdutos = totalProdutos;
	}
	public BigDecimal getTaxaServico() {
		return taxaServico;
	}
	public void setTaxaServico(BigDecimal taxaServico) {
		this.taxaServico = taxaServico;
	}
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
	public Loja getLoja() {
		return loja;
	}
	public void setLoja(Loja loja) {
		this.loja = loja;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Conta other = (Conta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
    
    
    
}
