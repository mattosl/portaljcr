package com.totvs.technology.ecm.dm.ws;

public class CardServiceProxy implements com.totvs.technology.ecm.dm.ws.CardService {
  private String _endpoint = null;
  private com.totvs.technology.ecm.dm.ws.CardService cardService = null;
  
  public CardServiceProxy() {
    _initCardServiceProxy();
  }
  
  public CardServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCardServiceProxy();
  }
  
  private void _initCardServiceProxy() {
    try {
      cardService = (new com.totvs.technology.ecm.dm.ws.ECMCardServiceServiceLocator()).getCardServicePort();
      if (cardService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)cardService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)cardService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (cardService != null)
      ((javax.xml.rpc.Stub)cardService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.totvs.technology.ecm.dm.ws.CardService getCardService() {
    if (cardService == null)
      _initCardServiceProxy();
    return cardService;
  }
  
  public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] create(int companyId, java.lang.String username, java.lang.String password, com.totvs.technology.ecm.dm.ws.CardDto[] card) throws java.rmi.RemoteException{
    if (cardService == null)
      _initCardServiceProxy();
    return cardService.create(companyId, username, password, card);
  }
  
  public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] updateCardData(int companyId, java.lang.String username, java.lang.String password, int cardId, com.totvs.technology.ecm.dm.ws.CardFieldDto[] cardData) throws java.rmi.RemoteException{
    if (cardService == null)
      _initCardServiceProxy();
    return cardService.updateCardData(companyId, username, password, cardId, cardData);
  }
  
  public com.totvs.technology.ecm.dm.ws.CardDto[] getCardVersion(int companyId, java.lang.String username, java.lang.String password, int documentId, int version, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.dm.ws.Exception{
    if (cardService == null)
      _initCardServiceProxy();
    return cardService.getCardVersion(companyId, username, password, documentId, version, colleagueId);
  }
  
  public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] updateCard(int companyId, java.lang.String username, java.lang.String password, com.totvs.technology.ecm.dm.ws.CardDto[] card, com.totvs.technology.ecm.dm.ws.Attachment[] attachments, com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] security, com.totvs.technology.ecm.dm.ws.ApproverDto[] approvers, com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] relatedDocuments) throws java.rmi.RemoteException, com.totvs.technology.ecm.dm.ws.Exception{
    if (cardService == null)
      _initCardServiceProxy();
    return cardService.updateCard(companyId, username, password, card, attachments, security, approvers, relatedDocuments);
  }
  
  public com.totvs.technology.ecm.dm.ws.WebServiceMessage[] deleteCard(int companyId, java.lang.String username, java.lang.String password, int cardId) throws java.rmi.RemoteException{
    if (cardService == null)
      _initCardServiceProxy();
    return cardService.deleteCard(companyId, username, password, cardId);
  }
  
  
}