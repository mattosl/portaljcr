package br.com.grupojcr.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

public class Preferencias implements Serializable {

	private static final long serialVersionUID = -6198795741392010334L;

	public enum Propriedades {
		
		URL_PORTAL					("url.portal"),
		AMBIENTE_FLUIG				("ambiente.fluig"),
		AMBIENTE_RM					("ambiente.rm"),
		USUARIO_FLUIG				("usuario.fluig"),
		CHAVE_FLUIG					("chave.fluig"),
		FLUIG_SOLICITACAO_COMPRA	("processo.fluig.solicitacao.compra");

		private String key;

		private Propriedades(String n) {
			this.key = n;
		}
	}

	public static String get(Propriedades propriedade) {
		return getProperty(propriedade.key);
	}
	
	public static String get(String propriedade) {
		return getProperty(propriedade);
	}

	private static final String getProperty(String key) {
		String fileName = "/enviroment.properties";
		InputStream in = null;
		String resul = null;
		try {
			Properties prop = new Properties();
			in = Preferencias.class.getResourceAsStream(fileName);
			if (in == null) {
				throw new FileNotFoundException(fileName);
			}
			prop.load(in);
			resul = (String) prop.get(key);
			if (resul == null || (resul = resul.trim()).equals("")) {
				throw new IllegalArgumentException("Propriedade " + key + " não definida no arquivo " + fileName);
			}
			return resul;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
