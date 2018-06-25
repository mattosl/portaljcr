package br.com.totvs.www.br;

public class WsDataServerSoapProxy implements br.com.totvs.www.br.WsDataServerSoap {
  private String _endpoint = null;
  private br.com.totvs.www.br.WsDataServerSoap wsDataServerSoap = null;
  
  public WsDataServerSoapProxy() {
    _initWsDataServerSoapProxy();
  }
  
  public WsDataServerSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initWsDataServerSoapProxy();
  }
  
  private void _initWsDataServerSoapProxy() {
    try {
      wsDataServerSoap = (new br.com.totvs.www.br.WsDataServerLocator()).getwsDataServerSoap();
      if (wsDataServerSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wsDataServerSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wsDataServerSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wsDataServerSoap != null)
      ((javax.xml.rpc.Stub)wsDataServerSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public br.com.totvs.www.br.WsDataServerSoap getWsDataServerSoap() {
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap;
  }
  
  public java.lang.String autenticaAcesso() throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.autenticaAcesso();
  }
  
  public java.lang.String autenticaAcessoAuth(java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.autenticaAcessoAuth(usuario, senha);
  }
  
  public java.lang.String getSchema(java.lang.String dataServerName, java.lang.String contexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.getSchema(dataServerName, contexto);
  }
  
  public java.lang.String getSchemaAuth(java.lang.String dataServerName, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.getSchemaAuth(dataServerName, contexto, usuario, senha);
  }
  
  public java.lang.String readView(java.lang.String dataServerName, java.lang.String filtro, java.lang.String contexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.readView(dataServerName, filtro, contexto);
  }
  
  public java.lang.String readViewAuth(java.lang.String dataServerName, java.lang.String filtro, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.readViewAuth(dataServerName, filtro, contexto, usuario, senha);
  }
  
  public java.lang.String readRecord(java.lang.String dataServerName, java.lang.String primaryKey, java.lang.String contexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.readRecord(dataServerName, primaryKey, contexto);
  }
  
  public java.lang.String readRecordAuth(java.lang.String dataServerName, java.lang.String primaryKey, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.readRecordAuth(dataServerName, primaryKey, contexto, usuario, senha);
  }
  
  public java.lang.String saveRecord(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.saveRecord(dataServerName, XML, contexto);
  }
  
  public java.lang.String saveRecordAuth(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto, java.lang.String usuario, java.lang.String senha) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.saveRecordAuth(dataServerName, XML, contexto, usuario, senha);
  }
  
  public java.lang.String deleteRecord(java.lang.String dataServerName, java.lang.String XML, java.lang.String contexto, java.lang.String emailUsuarioContexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.deleteRecord(dataServerName, XML, contexto, emailUsuarioContexto);
  }
  
  public java.lang.String deleteRecordAuth(java.lang.String dataServerName, java.lang.String XML, java.lang.String userName, java.lang.String userPassword, java.lang.String contexto, java.lang.String emailUsuarioContexto) throws java.rmi.RemoteException{
    if (wsDataServerSoap == null)
      _initWsDataServerSoapProxy();
    return wsDataServerSoap.deleteRecordAuth(dataServerName, XML, userName, userPassword, contexto, emailUsuarioContexto);
  }
  
  
}