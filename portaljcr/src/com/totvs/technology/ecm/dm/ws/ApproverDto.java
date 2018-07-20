/**
 * ApproverDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public class ApproverDto  implements java.io.Serializable {
    private int approvelMode;

    private int approverType;

    private java.lang.String colleagueId;

    private long companyId;

    private int documentId;

    private java.lang.String[] foo;

    private java.lang.String levelDescription;

    private int levelId;

    private int version;

    public ApproverDto() {
    }

    public ApproverDto(
           int approvelMode,
           int approverType,
           java.lang.String colleagueId,
           long companyId,
           int documentId,
           java.lang.String[] foo,
           java.lang.String levelDescription,
           int levelId,
           int version) {
           this.approvelMode = approvelMode;
           this.approverType = approverType;
           this.colleagueId = colleagueId;
           this.companyId = companyId;
           this.documentId = documentId;
           this.foo = foo;
           this.levelDescription = levelDescription;
           this.levelId = levelId;
           this.version = version;
    }


    /**
     * Gets the approvelMode value for this ApproverDto.
     * 
     * @return approvelMode
     */
    public int getApprovelMode() {
        return approvelMode;
    }


    /**
     * Sets the approvelMode value for this ApproverDto.
     * 
     * @param approvelMode
     */
    public void setApprovelMode(int approvelMode) {
        this.approvelMode = approvelMode;
    }


    /**
     * Gets the approverType value for this ApproverDto.
     * 
     * @return approverType
     */
    public int getApproverType() {
        return approverType;
    }


    /**
     * Sets the approverType value for this ApproverDto.
     * 
     * @param approverType
     */
    public void setApproverType(int approverType) {
        this.approverType = approverType;
    }


    /**
     * Gets the colleagueId value for this ApproverDto.
     * 
     * @return colleagueId
     */
    public java.lang.String getColleagueId() {
        return colleagueId;
    }


    /**
     * Sets the colleagueId value for this ApproverDto.
     * 
     * @param colleagueId
     */
    public void setColleagueId(java.lang.String colleagueId) {
        this.colleagueId = colleagueId;
    }


    /**
     * Gets the companyId value for this ApproverDto.
     * 
     * @return companyId
     */
    public long getCompanyId() {
        return companyId;
    }


    /**
     * Sets the companyId value for this ApproverDto.
     * 
     * @param companyId
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }


    /**
     * Gets the documentId value for this ApproverDto.
     * 
     * @return documentId
     */
    public int getDocumentId() {
        return documentId;
    }


    /**
     * Sets the documentId value for this ApproverDto.
     * 
     * @param documentId
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }


    /**
     * Gets the foo value for this ApproverDto.
     * 
     * @return foo
     */
    public java.lang.String[] getFoo() {
        return foo;
    }


    /**
     * Sets the foo value for this ApproverDto.
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
     * Gets the levelDescription value for this ApproverDto.
     * 
     * @return levelDescription
     */
    public java.lang.String getLevelDescription() {
        return levelDescription;
    }


    /**
     * Sets the levelDescription value for this ApproverDto.
     * 
     * @param levelDescription
     */
    public void setLevelDescription(java.lang.String levelDescription) {
        this.levelDescription = levelDescription;
    }


    /**
     * Gets the levelId value for this ApproverDto.
     * 
     * @return levelId
     */
    public int getLevelId() {
        return levelId;
    }


    /**
     * Sets the levelId value for this ApproverDto.
     * 
     * @param levelId
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }


    /**
     * Gets the version value for this ApproverDto.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ApproverDto.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ApproverDto)) return false;
        ApproverDto other = (ApproverDto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.approvelMode == other.getApprovelMode() &&
            this.approverType == other.getApproverType() &&
            ((this.colleagueId==null && other.getColleagueId()==null) || 
             (this.colleagueId!=null &&
              this.colleagueId.equals(other.getColleagueId()))) &&
            this.companyId == other.getCompanyId() &&
            this.documentId == other.getDocumentId() &&
            ((this.foo==null && other.getFoo()==null) || 
             (this.foo!=null &&
              java.util.Arrays.equals(this.foo, other.getFoo()))) &&
            ((this.levelDescription==null && other.getLevelDescription()==null) || 
             (this.levelDescription!=null &&
              this.levelDescription.equals(other.getLevelDescription()))) &&
            this.levelId == other.getLevelId() &&
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
        _hashCode += getApprovelMode();
        _hashCode += getApproverType();
        if (getColleagueId() != null) {
            _hashCode += getColleagueId().hashCode();
        }
        _hashCode += new Long(getCompanyId()).hashCode();
        _hashCode += getDocumentId();
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
        if (getLevelDescription() != null) {
            _hashCode += getLevelDescription().hashCode();
        }
        _hashCode += getLevelId();
        _hashCode += getVersion();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ApproverDto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "approverDto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approvelMode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "approvelMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("approverType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "approverType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colleagueId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "colleagueId"));
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
        elemField.setFieldName("foo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "foo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("levelDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "levelDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("levelId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "levelId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
