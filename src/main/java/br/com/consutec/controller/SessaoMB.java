package br.com.consutec.controller;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import com.xpert.faces.utils.FacesMessageUtils;

import br.com.consutec.dao.SistemaDAO;
import br.com.consutec.dao.UsuarioDAO;
import br.com.consutec.models.Loja;
import br.com.consutec.models.Sistema;
import br.com.consutec.models.Usuario;

@Named
@SessionScoped
public class SessaoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3980631174061967396L;
	private Usuario usuario;
	private String alertaexpiracao;
	private String login;
	private String senha;
	private String chave;
	private Loja lojaSelecionada;

	public Loja getLojaSelecionada() {
		return lojaSelecionada;
	}

	public void setLojaSelecionada(Loja lojaSelecionada) {
		this.lojaSelecionada = lojaSelecionada;
	}

	@EJB
	private UsuarioDAO usuarioDAO;
	
	@EJB
	private SistemaDAO sistemaDAO;

	@PostConstruct
	private void init(){
		alertaexpiracao = "";
	}
	public boolean verificaLicenca() {
		boolean liberado = true;
		/*try {
			Sistema sistema = sistemaDAO.findByid(Long.valueOf("1"));
			String serial =  sistema.getChave();
			byte[] chave = "teresinaconsutec".getBytes("UTF-8");
			
			Key secretKey = new SecretKeySpec(chave, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			
			byte[] preMensagem = Base64.getDecoder().decode(serial);
			byte[] saidamsg = cipher.doFinal(preMensagem);
			String saida = new String(saidamsg);
			Date data = new Date(Long.valueOf(saida));
			Calendar calendario = Calendar.getInstance();
	        calendario.setTime(data);
	        calendario.add(Calendar.DAY_OF_MONTH, -5);
	        if(Calendar.getInstance().after(calendario)){
	        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        	alertaexpiracao = "| Sistema expira em: "+sdf.format(data)+" | ";
	        }
	        if(data.after(Calendar.getInstance().getTime())){
	             liberado = true;
	        }else{
	             liberado = false;
	        }
	        liberado = true;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}*/
		
		return liberado;
	}
	
	public void salvarLicenca(){
		try {
			Sistema sistema = new Sistema();
			sistema = sistemaDAO.findByid(Long.valueOf("1"));
			sistema.setChave(chave);
			sistemaDAO.update(sistema);
			FacesMessageUtils.info("Licença Atualizada com sucesso!");
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().getFlash().setKeepMessages(true);
			fc.getExternalContext().redirect("/bistroeasy/admin/main/index.jsf");
		} catch (Exception e) {
			FacesMessageUtils.info("Licença Atualizada com sucesso!");
			e.printStackTrace();
		}
	}

	public void logar() {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.getExternalContext().redirect("/bistroeasy/admin/main/index.jsf");
			if (verificaLicenca()) {
				usuario = usuarioDAO.buscaUsuario(login, senha, true);
				if (usuario.getId() == null) {
					FacesMessageUtils.error("Não foi possível logar: login/senha incorretos");
				} else {
					fc.getExternalContext().redirect("/bistroeasy/admin/main/index.jsf");
				}
			} else {
				FacesMessageUtils.error("Sistema Está com a licença vencida, favor contactar nosso setor comercial");
				RequestContext.getCurrentInstance().execute("PF('licenca').show();");
			}
		} catch (Exception e) {
			FacesMessageUtils.error("Não Foi possível realizar o Login");
		}
	}

	public void fecharSessao() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		try {
			context.getExternalContext().redirect("/bistroeasy/index.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getAlertaexpiracao() {
		return alertaexpiracao;
	}

	public void setAlertaexpiracao(String alertaexpiracao) {
		this.alertaexpiracao = alertaexpiracao;
	}
    
}
