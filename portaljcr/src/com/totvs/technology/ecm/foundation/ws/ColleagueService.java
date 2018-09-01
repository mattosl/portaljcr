/**
 * ColleagueService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.foundation.ws;

public interface ColleagueService extends java.rmi.Remote {
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleaguesMail(java.lang.String username, java.lang.String password, int companyId, java.lang.String mail) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String removeColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getSummaryColleagues(int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String validateColleagueLogin(int companyId, java.lang.String colleagueId, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String activateColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto getSimpleColleague(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public com.totvs.technology.ecm.foundation.ws.GroupDto[] getGroups(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String createColleague(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String updateColleague(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String createColleaguewithDependencies(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues, com.totvs.technology.ecm.foundation.ws.GroupDto[] groups, com.totvs.technology.ecm.foundation.ws.WorkflowRoleDto[] workflowRoles) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto getColleagueByLogin(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public byte[] getColleaguesCompressedData(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleagues(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String updateColleaguewithDependencies(java.lang.String username, java.lang.String password, int companyId, com.totvs.technology.ecm.foundation.ws.ColleagueDto[] colleagues, com.totvs.technology.ecm.foundation.ws.GroupDto[] groups, com.totvs.technology.ecm.foundation.ws.WorkflowRoleDto[] workflowRoles) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
    public java.lang.String createColleagueWithMap(java.lang.String username, java.lang.String password, java.lang.String colleagueXML) throws java.rmi.RemoteException;
    public com.totvs.technology.ecm.foundation.ws.ColleagueDto[] getColleague(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.foundation.ws.Exception;
}
