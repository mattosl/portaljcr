/**
 * DocumentSecurityConfigDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public class DocumentSecurityConfigDto  implements java.io.Serializable {
    private java.lang.Integer attributionType;

    private java.lang.String attributionValue;

    private long companyId;

    private int documentId;

    private java.lang.Boolean downloadEnabled;

    private java.lang.String[] foo;

    private java.lang.Boolean permission;

    private java.lang.Integer securityLevel;

    private java.lang.Boolean securityVersion;

    private int sequence;

    private java.lang.Boolean showContent;

    private int version;

    public DocumentSecurityConfigDto() {
    }

    public DocumentSecurityConfigDto(
           java.lang.Integer attributionType,
           java.lang.String attributionValue,
           long companyId,
           int documentId,
           java.lang.Boolean downloadEnabled,
           java.lang.String[] foo,
           java.lang.Boolean permission,
           java.lang.Integer securityLevel,
           java.lang.Boolean securityVersion,
           int sequence,
           java.lang.Boolean showContent,
           int version) {
           this.attributionType = attributionType;
           this.attributionValue = attributionValue;
           this.companyId = companyId;
           this.documentId = documentId;
           this.downloadEnabled = downloadEnabled;
           this.foo = foo;
           this.permission = permission;
           this.securityLevel = securityLevel;
           this.securityVersion = securityVersion;
           this.sequence = sequence;
           this.showContent = showContent;
           this.version = version;
    }


    /**
     * Gets the attributionType value for this DocumentSecurityConfigDto.
     * 
     * @return attributionType
     */
    public java.lang.Integer getAttributionType() {
        return attributionType;
    }


    /**
     * Sets the attributionType value for this DocumentSecurityConfigDto.
     * 
     * @param attributionType
     */
    public void setAttributionType(java.lang.Integer attributionType) {
        this.attributionType = attributionType;
    }


    /**
     * Gets the attributionValue value for this DocumentSecurityConfigDto.
     * 
     * @return attributionValue
     */
    public java.lang.String getAttributionValue() {
        return attributionValue;
    }


    /**
     * Sets the attributionValue value for this DocumentSecurityConfigDto.
     * 
     * @param attributionValue
     */
    public void setAttributionValue(java.lang.String attributionValue) {
        this.attributionValue = attributionValue;
    }


    /**
     * Gets the companyId value for this DocumentSecurityConfigDto.
     * 
     * @return companyId
     */
    public long getCompanyId() {
        return companyId;
    }


    /**
     * Sets the companyId value for this DocumentSecurityConfigDto.
     * 
     * @param companyId
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }


    /**
     * Gets the documentId value for this DocumentSecurityConfigDto.
     * 
     * @return documentId
     */
    public int getDocumentId() {
        return documentId;
    }


    /**
     * Sets the documentId value for this DocumentSecurityConfigDto.
     * 
     * @param documentId
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }


    /**
     * Gets the downloadEnabled value for this DocumentSecurityConfigDto.
     * 
     * @return downloadEnabled
     */
    public java.lang.Boolean getDownloadEnabled() {
        return downloadEnabled;
    }


    /**
     * Sets the downloadEnabled value for this DocumentSecurityConfigDto.
     * 
     * @param downloadEnabled
     */
    public void setDownloadEnabled(java.lang.Boolean downloadEnabled) {
        this.downloadEnabled = downloadEnabled;
    }


    /**
     * Gets the foo value for this DocumentSecurityConfigDto.
     * 
     * @return foo
     */
    public java.lang.String[] getFoo() {
        return foo;
    }


    /**
     * Sets the foo value for this DocumentSecurityConfigDto.
     * 
     * @param foo
     */
    public void setFoo(java.lang.String[] foo) {
        this.foo = foo;
    }

    public java.lang.String getFoo(int i) {
        return this.foo[i];
    }

    public void setFoo(int i, java.lang.String _value) {
        this.foo[i] = _value;
    }


    /**
     * Gets the permission value for this DocumentSecurityConfigDto.
     * 
     * @return permission
     */
    public java.lang.Boolean getPermission() {
        return permission;
    }


    /**
     * Sets the permission value for this DocumentSecurityConfigDto.
     * 
     * @param permission
     */
    public void setPermission(java.lang.Boolean permission) {
        this.permission = permission;
    }


    /**
     * Gets the securityLevel value for this DocumentSecurityConfigDto.
     * 
     * @return securityLevel
     */
    public java.lang.Integer getSecurityLevel() {
        return securityLevel;
    }


    /**
     * Sets the securityLevel value for this DocumentSecurityConfigDto.
     * 
     * @param securityLevel
     */
    public void setSecurityLevel(java.lang.Integer securityLevel) {
        this.securityLevel = securityLevel;
    }


    /**
     * Gets the securityVersion value for this DocumentSecurityConfigDto.
     * 
     * @return securityVersion
     */
    public java.lang.Boolean getSecurityVersion() {
        return securityVersion;
    }


    /**
     * Sets the securityVersion value for this DocumentSecurityConfigDto.
     * 
     * @param securityVersion
     */
    public void setSecurityVersion(java.lang.Boolean securityVersion) {
        this.securityVersion = securityVersion;
    }


    /**
     * Gets the sequence value for this DocumentSecurityConfigDto.
     * 
     * @return sequence
     */
    public int getSequence() {
        return sequence;
    }


    /**
     * Sets the sequence value for this DocumentSecurityConfigDto.
     * 
     * @param sequence
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }


    /**
     * Gets the showContent value for this DocumentSecurityConfigDto.
     * 
     * @return showContent
     */
    public java.lang.Boolean getShowContent() {
        return showContent;
    }


    /**
     * Sets the showContent value for this DocumentSecurityConfigDto.
     * 
     * @param showContent
     */
    public void setShowContent(java.lang.Boolean showContent) {
        this.showContent = showContent;
    }


    /**
     * Gets the version value for this DocumentSecurityConfigDto.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this DocumentSecurityConfigDto.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentSecurityConfigDto)) return false;
        DocumentSecurityConfigDto other = (DocumentSecurityConfigDto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributionType==null && other.getAttributionType()==null) || 
             (this.attributionType!=null &&
              this.attributionType.equals(other.getAttributionType()))) &&
            ((this.attributionValue==null && other.getAttributionValue()==null) || 
             (this.attributionValue!=null &&
              this.attributionValue.equals(other.getAttributionValue()))) &&
            this.companyId == other.getCompanyId() &&
            this.documentId == other.getDocumentId() &&
            ((this.downloadEnabled==null && other.getDownloadEnabled()==null) || 
             (this.downloadEnabled!=null &&
              this.downloadEnabled.equals(other.getDownloadEnabled()))) &&
            ((this.foo==null && other.getFoo()==null) || 
             (this.foo!=null &&
              java.util.Arrays.equals(this.foo, other.getFoo()))) &&
            ((this.permission==null && other.getPermission()==null) || 
             (this.permission!=null &&
              this.permission.equals(other.getPermission()))) &&
            ((this.securityLevel==null && other.getSecurityLevel()==null) || 
             (this.securityLevel!=null &&
              this.securityLevel.equals(other.getSecurityLevel()))) &&
            ((this.securityVersion==null && other.getSecurityVersion()==null) || 
             (this.securityVersion!=null &&
              this.securityVersion.equals(other.getSecurityVersion()))) &&
            this.sequence == other.getSequence() &&
            ((this.showContent==null && other.getShowContent()==null) || 
             (this.showContent!=null &&
              this.showContent.equals(other.getShowContent()))) &&
            this.version == other.getVersion();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAttributionType() != null) {
            _hashCode += getAttributionType().hashCode();
        }
        if (getAttributionValue() != null) {
            _hashCode += getAttributionValue().hashCode();
        }
        _hashCode += new Long(getCompanyId()).hashCode();
        _hashCode += getDocumentId();
        if (getDownloadEnabled() != null) {
            _hashCode += getDownloadEnabled().hashCode();
        }
        if (getFoo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFoo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFoo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPermission() != null) {
            _hashCode += getPermission().hashCode();
        }
        if (getSecurityLevel() != null) {
            _hashCode += getSecurityLevel().hashCode();
        }
        if (getSecurityVersion() != null) {
            _hashCode += getSecurityVersion().hashCode();
        }
        _hashCode += getSequence();
        if (getShowContent() != null) {
            _hashCode += getShowContent().hashCode();
        }
        _hashCode += getVersion();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentSecurityConfigDto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "documentSecurityConfigDto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributionType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributionValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attributionValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("companyId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "companyId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadEnabled");
        elemField.setXmlName(new javax.xml.namespace.QName("", "downloadEnabled"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("foo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "foo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("permission");
        elemField.setXmlName(new javax.xml.namespace.QName("", "permission"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "securityLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "securityVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sequence");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sequence"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showContent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "showContent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
