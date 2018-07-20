/**
 * CardService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public interface CardService extends java.rmi.Remote {
    public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] create(int companyId, java.lang.String username, java.lang.String password, com.totvs.technology.ecm.dm.ws.CardDto[] card) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] updateCardData(int companyId, java.lang.String username, java.lang.String password, int cardId, com.totvs.technology.ecm.dm.ws.CardFieldDto[] cardData) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.dm.ws.CardDto[] getCardVersion(int companyId, java.lang.String username, java.lang.String password, int documentId, int version, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.dm.ws.Exception;
    public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] updateCard(int companyId, java.lang.String username, java.lang.String password, com.totvs.technology.ecm.dm.ws.CardDto[] card, com.totvs.technology.ecm.dm.ws.Attachment[] attachments, com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] security, com.totvs.technology.ecm.dm.ws.ApproverDto[] approvers, com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] relatedDocuments) throws java.rmi.RemoteException, com.totvs.technology.ecm.dm.ws.Exception;
    public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] deleteCard(int companyId, java.lang.String username, java.lang.String password, int cardId) throws java.rmi.RemoteException;
}
