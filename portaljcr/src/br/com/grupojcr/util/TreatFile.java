package br.com.grupojcr.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import br.com.grupojcr.util.exception.ApplicationException;


public class TreatFile {

	public static final String getContent(InputStream in) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(in, "ISO-8859-1"));
			String contentLine = null;
			StringBuilder content = new StringBuilder();
			
			while ((contentLine = reader.readLine()) != null) {
				content.append(contentLine);
			}
			
			return content.toString();
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
			
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


	/**
	 * @param folder
	 *            - deve ser terminada com "/" Salva arquivo na pasta
	 *            especificada. Caso a pasta nao exista, o metodo tentar
	 *            cria-la
	 */
	public static final File saveFileByFolder(InputStream in, String folder, String file) throws IOException {
		createDirectory(folder);
		OutputStream out = null;
		
		try {
			in = new BufferedInputStream(in);
			File fileNew = new File(folder + file);
			out = new BufferedOutputStream(new FileOutputStream(fileNew));
			IOUtils.copy(in, out);
			out.flush();
			
			return fileNew;
			
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * Cria o diretorio informado caso nao exista.
	 */
	public static boolean createDirectory(String folder) {
		File dir = new File(folder);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IllegalArgumentException( "Nao foi possivel criar diretorio [" + folder + "] nao criado.");
			}
			return true;
		}
		return false;
	}

	/**
	 * Identifica a extensao do nome do arquivo passado
	 */
	public static final String getTypeFile(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	}

	public static byte[] fileToByte(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for(int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			return bos.toByteArray();
		} catch (IOException ex) {
			throw ex;
		} finally {
			fis.close();
		}
	}
	
	public static final Boolean isImagem(String extensao) throws ApplicationException{
		try{
			
			if (StringUtils.isNotBlank(extensao)){
				String[] imagens = {"jpg", "JPG", "gif", "GIF", "png", "PNG"};
				
				for (String ext : imagens) {
					if(extensao.trim().equalsIgnoreCase(ext)){
						return true;
					}
				}
				
				return false;
			}else{
				throw new ApplicationException("mensagem.parametroInvalido", new String[] { "validar se é uma imagem" });
			}
			
		}catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("message.default");
		}
		
	}
	
	public static final Boolean isPdf(String extensao) throws ApplicationException{
		try{
			
			if (StringUtils.isNotBlank(extensao)){
				String[] imagens = {"pdf", "PDF"};
				
				for (String ext : imagens) {
					if(extensao.trim().equalsIgnoreCase(ext)){
						return true;
					}
				}
				
				return false;
			}else{
				throw new ApplicationException("mensagem.parametroInvalido", new String[] { "validar se é um pdf" });
			}
			
		}catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("message.default");
		}
		
	}
	
	public static String obterNomeArquivo(String caminho) throws ApplicationException {
		try{
			if (caminho == null || caminho.indexOf(".") < 0) {
				return "";
			}
	
			String nomeArquivo = caminho.substring((caminho.lastIndexOf("/") + 1), caminho.lastIndexOf("."));
			return nomeArquivo.trim() + "." + TreatFile.getTypeFile(caminho);
		}catch (Exception e) {
			throw new ApplicationException("message.default");
		}
	}
}
