package br.com.grupojcr.ad;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import br.com.grupojcr.util.exception.ApplicationException;

public class ActiveDirectory {
	
	private final static String KEY_MENSAGEM_PADRAO = "message.default.erro";
    private final static String URL_LDAP = "ldap://10.0.0.51:389";
    
    
    public static DirContext getContext(String usuario, String password) throws ApplicationException {
    	try {
	        Hashtable<String, String> env = new Hashtable<String, String>();
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	        env.put(Context.PROVIDER_URL, URL_LDAP);
	        env.put(Context.SECURITY_AUTHENTICATION, "simple");
	        env.put(Context.SECURITY_PRINCIPAL, "GRUPOJCR\\" + usuario);
	        env.put(Context.SECURITY_CREDENTIALS, password);

			return new InitialDirContext(env);
		} catch (Exception e) {
			throw new ApplicationException(KEY_MENSAGEM_PADRAO, new String[] { "getContext" }, e);
		}
    	
    }
    
    @SuppressWarnings("rawtypes")
	public static UsuarioLDAP login(String usuario, String password) {
    	try {
            DirContext ctx = getContext(usuario, password);

            if(ctx != null) {
            	SearchControls constraints = new SearchControls();
	            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
	            String[] attrIDs = { "sAMAccountName",
	                    "mail",
	                    "displayName"};
	            constraints.setReturningAttributes(attrIDs);
	            //First input parameter is search bas, it can be "CN=Users,DC=YourDomain,DC=com"
	            //Second Attribute can be uid=username
	            NamingEnumeration answer = ctx.search("DC=grupojcr,DC=local", "samaccountname=" + usuario, constraints);
	            if (answer.hasMore()) {
	                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
	                UsuarioLDAP usuarioLdap = new UsuarioLDAP();
	                usuarioLdap.setNomeCompleto((String) attrs.get("displayName").get());
	                usuarioLdap.setUsuario((String) attrs.get("sAMAccountName").get());
	                usuarioLdap.setEmail((String) attrs.get("mail").get());
	                
	                ctx.close();
	                return usuarioLdap;
	            }
	            ctx.close();
            }

            return null;
        } catch (Exception e) {           
            return null;
        }
    }
    
}
