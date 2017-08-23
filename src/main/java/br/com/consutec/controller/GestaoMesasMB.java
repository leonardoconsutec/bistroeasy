package br.com.consutec.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.component.commandbutton.CommandButton;

import br.com.consutec.dao.MesaDAO;
import br.com.consutec.models.Mesa;

@ManagedBean
@ViewScoped
public class GestaoMesasMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7689563692839768151L;
	
	@EJB
	private MesaDAO mesaDAO;
	
	@Inject
	protected SessaoMB sessao;
	
	private List<Mesa> mesas;
	private HtmlPanelGrid grid;

	@PostConstruct
	private void init(){
		gerarListaMesas();
	}
	
	
	private void gerarListaMesas(){
		try {
			grid = new HtmlPanelGrid();
			
			mesas = mesaDAO.listarMesasLoja(sessao.getLojaSelecionada());
			for(Mesa ms : mesas){
				CommandButton btn = new CommandButton();
				//btn.setUpdate(":detalhesMesa");
				FacesContext context =FacesContext.getCurrentInstance();
			    ELContext elContext = context.getELContext();
			    String metodo = String.format("#{contaMB.verificaMesa('%s')}", ms.getId().toString());
			    MethodExpression expression = ExpressionFactory.newInstance().createMethodExpression(elContext, metodo, null, null);
			    
				btn.setValue(ms.getSequencial().toString());
				btn.setRendered(true);
				btn.setActionExpression(expression);;
				if(ms.getStatus().equals(Long.valueOf("0"))){
					btn.setStyleClass("styleRed");
				}else{
					if(ms.getStatus().equals(Long.valueOf("1"))){
						btn.setStyleClass("styleGreen");
					}else{
						btn.setStyleClass("styleYellow");
					}
				}
				
				btn.setId("mesa"+ms.getId());
				grid.getChildren().add(btn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Mesa> getMesas() {
		return mesas;
	}
	public void setMesas(List<Mesa> mesas) {
		this.mesas = mesas;
	}

	public HtmlPanelGrid getGrid() {
		return grid;
	}

	public void setGrid(HtmlPanelGrid grid) {
		this.grid = grid;
	}
	
	
	

}
