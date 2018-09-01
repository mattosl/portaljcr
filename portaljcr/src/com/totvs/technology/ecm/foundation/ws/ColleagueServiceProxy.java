package com.totvs.technology.ecm.foundation.ws;

public class ColleagueServiceProxy implements com.totvs.technology.ecm.foundation.ws.ColleagueService {
  private String _endpoint = null;
  private com.totvs.technology.ecm.foundation.ws.ColleagueService colleagueService = null;
  
  public ColleagueServiceProxy() {
    _initColleagueServiceProxy();
  }
  
  public ColleagueServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initColleagueServiceProxy();
  }
  
  private void _initColleagueServiceProxy() {
    try {
      colleagueService = (new com.totvs.technology.ecm.foundation.ws.ECMColleagueServiceServiceLocator()).getColleagueServicePort();
      if (colleagueService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)colleagueService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)colleagueService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (colleagueService != null)
      ((javax.xml.rpc.Stub)colleagueService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueService getColleagueService() {
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService;
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleaguesMail(java.lang.String username, java.lang.String password, int companyId, java.lang.String mail) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getColleaguesMail(username, password, companyId, mail);
  }
  
  public java.lang.String removeColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.removeColleague(username, password, companyId, colleagueId);
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getSummaryColleagues(int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getSummaryColleagues(companyId);
  }
  
  public java.lang.String validateColleagueLogin(int companyId, java.lang.String colleagueId, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.validateColleagueLogin(companyId, colleagueId, password);
  }
  
  public java.lang.String activateColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.activateColleague(username, password, companyId, colleagueId);
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto getSimpleColleague(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getSimpleColleague(username, password);
  }
  
  public com.totvs.technology.ecm.foundation.ws.GroupDto[] getGroups(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getGroups(username, password, companyId, colleagueId);
  }
  
  public java.lang.String createColleague(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.createColleague(username, password, companyId, colleagues);
  }
  
  public java.lang.String updateColleague(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.updateColleague(username, password, companyId, colleagues);
  }
  
  public java.lang.String createColleaguewithDependencies(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues, com.totvs.technology.ecm.foundation.ws.GroupDto[] groups, com.totvs.technology.ecm.foundation.ws.WorkflowRoleDto[] workflowRoles) throws java.rmi.RemoteException{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.createColleaguewithDependencies(username, password, companyId, colleagues, groups, workflowRoles);
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto getColleagueByLogin(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getColleagueByLogin(username, password);
  }
  
  public byte[] getColleaguesCompressedData(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getColleaguesCompressedData(username, password, companyId);
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleagues(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getColleagues(username, password, companyId);
  }
  
  public java.lang.String updateColleaguewithDependencies(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues, com.totvs.technology.ecm.foundation.ws.GroupDto[] groups, com.totvs.technology.ecm.foundation.ws.WorkflowRoleDto[] workflowRoles) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.updateColleaguewithDependencies(username, password, companyId, colleagues, groups, workflowRoles);
  }
  
  public java.lang.String createColleagueWithMap(java.lang.String username, java.lang.String password, java.lang.String colleagueXML) throws java.rmi.RemoteException{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.createColleagueWithMap(username, password, colleagueXML);
  }
  
  public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception{
    if (colleagueService == null)
      _initColleagueServiceProxy();
    return colleagueService.getColleague(username, password, companyId, colleagueId);
  }
  
  
}