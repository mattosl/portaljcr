/**
 * ECMCardServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public interface ECMCardServiceService extends javax.xml.rpc.Service {
    public java.lang.String getCardServicePortAddress();

    public com.totvs.technology.ecm.dm.ws.CardService getCardServicePort() throws javax.xml.rpc.ServiceException;

    public com.totvs.technology.ecm.dm.ws.CardService getCardServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
