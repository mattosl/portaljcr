/**
 * WsDataServer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.totvs.www.br;

public interface WsDataServer extends javax.xml.rpc.Service {

/**
 * Este serviço disponibiliza acesso direto aos objetos de negócio
 * RM (DataServer). 
 *       <br/><b>Os WebMethods com nomes terminados em Auth utilizam
 * autenticação com login e senha passados por parâmetros, os restantes
 * utilizam autenticação SOAP.</b>
 */
    public java.lang.String getwsDataServerSoapAddress();

    public br.com.totvs.www.br.WsDataServerSoap getwsDataServerSoap() throws javax.xml.rpc.ServiceException;

    public br.com.totvs.www.br.WsDataServerSoap getwsDataServerSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
