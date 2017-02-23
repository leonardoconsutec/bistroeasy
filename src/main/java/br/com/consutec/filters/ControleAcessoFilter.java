package br.com.consutec.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import br.com.consutec.controller.SessaoMB;

@WebFilter(urlPatterns="/admin/*")
public class ControleAcessoFilter implements Filter{
	
	@Inject
	protected SessaoMB sessao;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		if(sessao.getUsuario() == null){
			resp.sendRedirect("/bistroeasy/index.jsf");
		}
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
