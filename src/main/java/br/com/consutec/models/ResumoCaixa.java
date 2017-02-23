package br.com.consutec.models;

import java.math.BigDecimal;

public class ResumoCaixa {
	private BigDecimal dinheiro;
	private BigDecimal cartao;
	private BigDecimal cheque;
	private BigDecimal convenio;

	public BigDecimal getDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(BigDecimal dinheiro) {
		this.dinheiro = dinheiro;
	}

	public BigDecimal getCartao() {
		return cartao;
	}

	public void setCartao(BigDecimal cartao) {
		this.cartao = cartao;
	}

	public BigDecimal getCheque() {
		return cheque;
	}

	public void setCheque(BigDecimal cheque) {
		this.cheque = cheque;
	}

	public BigDecimal getConvenio() {
		return convenio;
	}

	public void setConvenio(BigDecimal convenio) {
		this.convenio = convenio;
	}

}
