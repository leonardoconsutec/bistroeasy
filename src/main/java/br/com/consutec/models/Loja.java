package br.com.consutec.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="loja")
public class Loja implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3887208750435650526L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(name="nomeFantasia", length = 40)
    private String nomeFantasia;
    @Column(name="razaoSocial", length = 40)
    private String razaoSocial;
    @Column(name="cnpj", length = 20)
    private String cnpj;
    @Column(name="cep", length = 15)
    private String cep;
    @Column(name = "logradouro", length = 40)
    private String logradouro;
    @Column(name = "numero", length = 10)
    private String numero;
    @Column(name = "bairro", length = 40)
    private String bairro;
    @Column(name = "cidade", length = 40)
    private String cidade;
    @Column(name = "uf", length = 2)
    private String uf;
    @Column(name = "inscricaoEstadual", length = 20)
    private String inscricaoEstadual;
    @Column(name = "inscricaoMunicipal", length = 20)
    private String inscricaoMunicipal;
    @Column(name = "telefone", length = 15)
    private String telefone;
    @OneToOne
    @JoinColumn(name="empresa")
    private Empresa empresa;
    @Column(name="ativa")
    private boolean ativa;
    @Column(name="usaTaxaServicos")
    private boolean usaTaxaServicos;
    @Column(name="taxaServicos")
    private BigDecimal taxaServicos;
    @Column(name="descontoMaximo")
    private BigDecimal descontoMaximo;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}
	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public boolean isAtiva() {
		return ativa;
	}
	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	public boolean isUsaTaxaServicos() {
		return usaTaxaServicos;
	}
	public void setUsaTaxaServicos(boolean usaTaxaServicos) {
		this.usaTaxaServicos = usaTaxaServicos;
	}
	public BigDecimal getTaxaServicos() {
		return taxaServicos;
	}
	public void setTaxaServicos(BigDecimal taxaServicos) {
		this.taxaServicos = taxaServicos;
	}
	
	public BigDecimal getDescontoMaximo() {
		return descontoMaximo;
	}
	public void setDescontoMaximo(BigDecimal descontoMaximo) {
		this.descontoMaximo = descontoMaximo;
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
		Loja other = (Loja) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
    
    
}
