/**
 * WsDataServerLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package br.com.totvs.www.br;

public class WsDataServerLocator extends org.apache.axis.client.Service implements br.com.totvs.www.br.WsDataServer {

/**
 * Este serviço disponibiliza acesso direto aos objetos de negócio
 * RM (DataServer). 
 *       <br/><b>Os WebMethods com nomes terminados em Auth utilizam
 * autenticação com login e senha passados por parâmetros, os restantes
 * utilizam autenticação SOAP.</b>
 */

    public WsDataServerLocator() {
    }


    public WsDataServerLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsDataServerLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for wsDataServerSoap
    private java.lang.String wsDataServerSoap_address = "http://10.0.0.48/TOTVSBusinessConnect/wsDataServer.asmx";

    public java.lang.String getwsDataServerSoapAddress() {
        return wsDataServerSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String wsDataServerSoapWSDDServiceName = "wsDataServerSoap";

    public java.lang.String getwsDataServerSoapWSDDServiceName() {
        return wsDataServerSoapWSDDServiceName;
    }

    public void setwsDataServerSoapWSDDServiceName(java.lang.String name) {
        wsDataServerSoapWSDDServiceName = name;
    }

    public br.com.totvs.www.br.WsDataServerSoap getwsDataServerSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(wsDataServerSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getwsDataServerSoap(endpoint);
    }

    public br.com.totvs.www.br.WsDataServerSoap getwsDataServerSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            br.com.totvs.www.br.WsDataServerSoapStub _stub = new br.com.totvs.www.br.WsDataServerSoapStub(portAddress, this);
            _stub.setPortName(getwsDataServerSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setwsDataServerSoapEndpointAddress(java.lang.String address) {
        wsDataServerSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (br.com.totvs.www.br.WsDataServerSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                br.com.totvs.www.br.WsDataServerSoapStub _stub = new br.com.totvs.www.br.WsDataServerSoapStub(new java.net.URL(wsDataServerSoap_address), this);
                _stub.setPortName(getwsDataServerSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("wsDataServerSoap".equals(inputPortName)) {
            return getwsDataServerSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.totvs.com.br/br/", "wsDataServer");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.totvs.com.br/br/", "wsDataServerSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("wsDataServerSoap".equals(portName)) {
            setwsDataServerSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
