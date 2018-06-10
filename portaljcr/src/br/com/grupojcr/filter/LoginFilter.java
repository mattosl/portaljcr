package br.com.grupojcr.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.grupojcr.controller.LoginController;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
			throws IOException, ServletException {

		LoginController loginBean = (LoginController) ((HttpServletRequest) request).getSession().getAttribute("loginController");
		
		// For the first application request there is no loginBean in the
		// session so user needs to log in
		// For other requests loginBean is present but we need to check if user
		// has logged in successfully
		HttpSession session = ((HttpServletRequest) request).getSession(true);
		
		String parametros = ((HttpServletRequest) request).getQueryString();
		
		String newCurrentPage = ((HttpServletRequest) request).getServletPath();
		if (session.getAttribute("currentPage") == null) {
			session.setAttribute("lastPage", newCurrentPage);
			session.setAttribute("currentPage", newCurrentPage);
			session.setAttribute("parametros", parametros);
		} else {
			String oldCurrentPage = session.getAttribute("currentPage").toString();
			if (!oldCurrentPage.equals(newCurrentPage)) {
				session.setAttribute("lastPage", oldCurrentPage);
				session.setAttribute("currentPage", newCurrentPage);
				session.setAttribute("parametros", parametros);
			}
		}
		
		if (loginBean == null || !loginBean.isLogado()) {
			String contextPath = ((HttpServletRequest) request).getContextPath();
			((HttpServletResponse) response).sendRedirect(contextPath + "/login.xhtml");
		} else {
			filter.doFilter(request, response);
		}
		

		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
