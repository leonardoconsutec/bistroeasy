package br.com.consutec.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.xpert.faces.utils.FacesMessageUtils;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class relatoriosUtil {
	public static void imprimeRelatorio(String nomeRelatorio, HashMap parametros, List lista) {
		try {
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
			FacesContext fc = FacesContext.getCurrentInstance();
			ServletContext sc = (ServletContext) fc.getExternalContext().getContext();
			String path = sc.getRealPath("/WEB-INF/relatorios/");
			parametros.put("SUBREPORT_DIR", path + File.separator);
			JasperPrint jp = JasperFillManager.fillReport(
					sc.getRealPath("/WEB-INF/relatorios/" + nomeRelatorio + ".jasper"), parametros, dataSource);
			byte[] b = JasperExportManager.exportReportToPdf(jp);
			HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();
			resp.setContentType("application/pdf");
			int codigo = (int) (Math.random() * 1000);
			resp.setHeader("Content-disposition", "inline);filename=relatorio_"+codigo+".pdf");
			resp.getOutputStream().write(b);
			fc.responseComplete();
		} catch (Exception e) {
			//FacesMessageUtils.error("Erro ao imprimir!");
			e.printStackTrace();
		}

	}

}
