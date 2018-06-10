package br.com.grupojcr.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
public class NavegacaoController implements Serializable {

	private static final long serialVersionUID = 6613367231048373566L;
	
	 /**
     * Redirect to login page.
     * @return Login page name.
     */
    public String redirectToLogin() {
        return "/login.xhtml?faces-redirect=true";
    }
     
    /**
     * Go to login page.
     * @return Login page name.
     */
    public String toLogin() {
        return "/login.xhtml";
    }
    
    /**
     * Redirect to welcome page.
     * @return Welcome page name.
     */
    public String redirectToWelcome() {
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpSession session = request.getSession(true);
    	
    	if(session.getAttribute("lastPage") != null) {
    		String parametros = null;
    				
			if(session.getAttribute("parametros") != null) {
				parametros = session.getAttribute("parametros").toString();
				
				return session.getAttribute("lastPage").toString() + "?faces-redirect=true&" + parametros;
			}
    		return session.getAttribute("lastPage").toString() + "?faces-redirect=true&";
    	}
        return "/pages/login/welcome.xhtml?faces-redirect=true";
    }
     
    /**
     * Go to welcome page.
     * @return Welcome page name.
     */
    public String toWelcome() {
        return "/pages/login/welcome.xhtml";
    }
     
	

}
