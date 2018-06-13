/**
 * WorkflowEngineService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.workflow.ws;

public interface WorkflowEngineService extends java.rmi.Remote {
    public java.lang.String importProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, com.totvs.technology.ecm.workflow.ws.Attachment[] attachments, boolean newProcess, boolean overWrite, java.lang.String colleagueId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String setDueDate(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String userId, int threadSequence, java.lang.String newDueDate, int timeInSecods) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String takeProcessTask(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[][] getInstanceCardData(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String createWorkFlowProcessVersion(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String getCardValue(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String userId, java.lang.String cardFieldName) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessDefinitionDto[] getAllProcessAvailableToExport(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[][] saveAndSendTask(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int choosedState, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean completeTask, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, java.lang.String[][] cardData, com.totvs.technology.ecm.workflow.ws.ProcessTaskAppointmentDto[] appointment, boolean managerMode, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.KeyValueDto[] setAutomaticDecisionClassic(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int iTaskAutom, int iTask, int condition, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean managerMode, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.WebServiceMessage importProcessWithCardAndRelatedDatasets(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, com.totvs.technology.ecm.workflow.ws.Attachment[] processAttachs, boolean newProcess, boolean overWrite, java.lang.String colleagueId, int parentDocumentId, java.lang.String documentDescription, java.lang.String cardDescription, java.lang.String datasetName, com.totvs.technology.ecm.workflow.ws.Attachment[] cardAttachs, com.totvs.technology.ecm.workflow.ws.CardEventDto[] customEvents, com.totvs.technology.ecm.workflow.ws.GeneralInfoDto generalInfo, boolean update, int persistenceType, java.lang.String[] relatedDatasets) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.DeadLineDto calculateDeadLineHours(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, java.lang.String data, int hora, int prazo, java.lang.String periodId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessDefinitionDto[] getAllProcessAvailableToImport(java.lang.String username, java.lang.String password, int companyId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String cancelInstance(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String userId, java.lang.String cancelText) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public int getActualThread(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int stateSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public int getWorkFlowProcessVersion(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessDefinitionVersionDto[] getAvailableProcessOnDemand(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int limit, int lastRowId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessStateDto[] getAvailableStatesDetail(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int processInstanceId, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public byte[] exportProcessInZipFormat(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.WebServiceMessage importProcessWithCard(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, com.totvs.technology.ecm.workflow.ws.Attachment[] processAttachs, boolean newProcess, boolean overWrite, java.lang.String colleagueId, int parentDocumentId, java.lang.String documentDescription, java.lang.String cardDescription, java.lang.String datasetName, com.totvs.technology.ecm.workflow.ws.Attachment[] cardAttachs, com.totvs.technology.ecm.workflow.ws.CardEventDto[] customEvents, boolean update) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[][] saveAndSendTaskByReplacement(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int choosedState, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean completeTask, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, java.lang.String[][] cardData, com.totvs.technology.ecm.workflow.ws.ProcessTaskAppointmentDto[] appointment, boolean managerMode, int threadSequence, java.lang.String replacementId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.KeyValueDto[] startProcessClassic(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int choosedState, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean completeTask, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, com.totvs.technology.ecm.workflow.ws.KeyValueDto[] cardData, com.totvs.technology.ecm.workflow.ws.ProcessTaskAppointmentDto[] appointment, boolean managerMode) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String cancelInstanceByReplacement(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String userId, java.lang.String cancelText, java.lang.String replacementId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.DeadLineDto calculateDeadLineTime(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, java.lang.String data, int hora, int prazo, java.lang.String periodId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[] simpleStartProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, java.lang.String comments, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, java.lang.String[][] cardData) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessDefinitionVersionDto[] getAvailableProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[][] startProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int choosedState, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean completeTask, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, java.lang.String[][] cardData, com.totvs.technology.ecm.workflow.ws.ProcessTaskAppointmentDto[] appointment, boolean managerMode) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public int[] getAllActiveStates(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String releaseProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String setTasksComments(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String userId, int threadSequence, java.lang.String comments) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessDefinitionVersionDto[] searchProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String colleagueId, java.lang.String content, boolean favorite) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.AvailableUsersDto getAvailableUsersOnDemand(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int state, int threadSequence, int limit, int initialUser, java.lang.String userSearch) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public int getProcessFormId(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.KeyValueDto[] saveAndSendTaskClassic(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int choosedState, java.lang.String[] colleagueIds, java.lang.String comments, java.lang.String userId, boolean completeTask, com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] attachments, com.totvs.technology.ecm.workflow.ws.KeyValueDto[] cardData, com.totvs.technology.ecm.workflow.ws.ProcessTaskAppointmentDto[] appointment, boolean managerMode, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.AvailableUsersDto getAvailableUsersStartOnDemand(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int state, int threadSequence, int limit, int initialUser, java.lang.String userSearch) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[] getAvailableUsers(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, int state, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String getProcessImage(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessAttachmentDto[] getAttachments(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.WebServiceMessage importProcessWithCardAndPersistenceType(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, com.totvs.technology.ecm.workflow.ws.Attachment[] processAttachs, boolean newProcess, boolean overWrite, java.lang.String colleagueId, int parentDocumentId, java.lang.String documentDescription, java.lang.String cardDescription, java.lang.String datasetName, com.totvs.technology.ecm.workflow.ws.Attachment[] cardAttachs, com.totvs.technology.ecm.workflow.ws.CardEventDto[] customEvents, boolean update, int persistenceType) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String[] getAvailableUsersStart(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int state, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.WebServiceMessage importProcessWithCardAndGeneralInfo(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, com.totvs.technology.ecm.workflow.ws.Attachment[] processAttachs, boolean newProcess, boolean overWrite, java.lang.String colleagueId, int parentDocumentId, java.lang.String documentDescription, java.lang.String cardDescription, java.lang.String datasetName, com.totvs.technology.ecm.workflow.ws.Attachment[] cardAttachs, com.totvs.technology.ecm.workflow.ws.CardEventDto[] customEvents, com.totvs.technology.ecm.workflow.ws.GeneralInfoDto generalInfo, boolean update, int persistenceType) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String takeProcessTaskByReplacement(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId, int threadSequence, java.lang.String replacementId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String exportProcess(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public int[] getAvailableStates(java.lang.String username, java.lang.String password, int companyId, java.lang.String processId, int processInstanceId, int threadSequence) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public java.lang.String updateWorkflowAttachment(java.lang.String username, java.lang.String password, int companyId, int processInstanceId, java.lang.String usuario, com.totvs.technology.ecm.workflow.ws.DocumentDto[] document, com.totvs.technology.ecm.workflow.ws.Attachment[] attachments) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
    public com.totvs.technology.ecm.workflow.ws.ProcessHistoryDto[] getHistories(java.lang.String username, java.lang.String password, int companyId, java.lang.String userId, int processInstanceId) throws java.rmi.RemoteException, com.totvs.technology.ecm.workflow.ws.Exception;
}
