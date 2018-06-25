/**
 * WsDataServerSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.totvs.www.br;

public interface WsDataServerSoap extends java.rmi.Remote {

    /**
     * Autentica o usuário no ambiente RM. 
     *           O usuário e senha devem ser informados via SOAP, criando
     * um Token para isto.
     */
    public java.lang.String autenticaAcesso() throws java.rmi.RemoteException;

    /**
     * Autentica o usuário no ambiente RM. 
     *           O usuário e senha devem ser informados por parâmetro.
     */
    public java.lang.String autenticaAcessoAuth(java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException;

    /**
     * Retorna o esquema (XSD) do DataServer (regra de negócio RM)
     * passado como parâmetro.
     */
    public java.lang.String getSchema(java.lang.String dataServerName, java.lang.String contexto) throws java.rmi.RemoteException;

    /**
     * Retorna o esquema (XSD) do DataServer (regra de negócio RM)
     * passado como parâmetro.
     */
    public java.lang.String getSchemaAuth(java.lang.String dataServerName, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException;

    /**
     * Faz a leitura do DataServer (regra de negócio RM) e retorna
     * conjunto de registros (XML), 
     *           conforme filtros informados nos parâmetros.
     */
    public java.lang.String readView(java.lang.String dataServerName, java.lang.String filtro, java.lang.String contexto) throws java.rmi.RemoteException;

    /**
     * Faz a leitura do DataServer (regra de negócio RM) e retorna
     * conjunto de registros (XML), 
     *           conforme filtros informados nos parâmetros.
     */
    public java.lang.String readViewAuth(java.lang.String dataServerName, java.lang.String filtro, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException;

    /**
     * Faz a leitura do DataServer (regra de negócio RM) e retorna
     * o registro específico (XML),
     *           conforme chave primária informada nos parâmetros.
     */
    public java.lang.String readRecord(java.lang.String dataServerName, java.lang.String primaryKey, java.lang.String contexto) throws java.rmi.RemoteException;

    /**
     * Faz a leitura do DataServer (regra de negócio RM) e retorna
     * o registro específico (XML),
     *           conforme chave primária informada nos parâmetros.
     */
    public java.lang.String readRecordAuth(java.lang.String dataServerName, java.lang.String primaryKey, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException;

    /**
     * Insere um registro no Datataserver (regra de negócio RM). 
     *           Este método somente irá incluir o registro se este for válido
     * de acordo com as regras de negócio definidas pelo DataServer.
     */
    public java.lang.String saveRecord(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto) throws java.rmi.RemoteException;

    /**
     * Insere um registro no Datataserver (regra de negócio RM). 
     *           Este método somente irá incluir o registro se este for válido
     * de acordo com as regras de negócio definidas pelo DataServer.
     */
    public java.lang.String saveRecordAuth(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException;

    /**
     * Exclui um registro no Datataserver (regra de negócio RM). 
     *           Este método somente irá exclui o registro se este for válido
     * de acordo com as regras de negócio definidas pelo DataServer.
     */
    public java.lang.String deleteRecord(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto, java.lang.String emailUsuarioContexto) throws java.rmi.RemoteException;

    /**
     * Exclui um registro no Datataserver (regra de negócio RM). 
     *           Este método somente irá exclui o registro se este for válido
     * de acordo com as regras de negócio definidas pelo DataServer.
     */
    public java.lang.String deleteRecordAuth(java.lang.String dataServerName, java.lang.String XML, java.lang.String userName, java.lang.String userPassword, java.lang.String contexto, java.lang.String emailUsuarioContexto) throws java.rmi.RemoteException;
}
