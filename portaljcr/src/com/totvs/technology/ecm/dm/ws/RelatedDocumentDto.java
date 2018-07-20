/**
 * RelatedDocumentDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public class RelatedDocumentDto  implements java.io.Serializable {
    private long companyId;

    private int documentId;

    private java.lang.String[] foo;

    private int relatedDocumentId;

    private int version;

    public RelatedDocumentDto() {
    }

    public RelatedDocumentDto(
           long companyId,
           int documentId,
           java.lang.String[] foo,
           int relatedDocumentId,
           int version) {
           this.companyId = companyId;
           this.documentId = documentId;
           this.foo = foo;
           this.relatedDocumentId = relatedDocumentId;
           this.version = version;
    }


    /**
     * Gets the companyId value for this RelatedDocumentDto.
     * 
     * @return companyId
     */
    public long getCompanyId() {
        return companyId;
    }


    /**
     * Sets the companyId value for this RelatedDocumentDto.
     * 
     * @param companyId
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }


    /**
     * Gets the documentId value for this RelatedDocumentDto.
     * 
     * @return documentId
     */
    public int getDocumentId() {
        return documentId;
    }


    /**
     * Sets the documentId value for this RelatedDocumentDto.
     * 
     * @param documentId
     */
    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }


    /**
     * Gets the foo value for this RelatedDocumentDto.
     * 
     * @return foo
     */
    public java.lang.String[] getFoo() {
        return foo;
    }


    /**
     * Sets the foo value for this RelatedDocumentDto.
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
     * Gets the relatedDocumentId value for this RelatedDocumentDto.
     * 
     * @return relatedDocumentId
     */
    public int getRelatedDocumentId() {
        return relatedDocumentId;
    }


    /**
     * Sets the relatedDocumentId value for this RelatedDocumentDto.
     * 
     * @param relatedDocumentId
     */
    public void setRelatedDocumentId(int relatedDocumentId) {
        this.relatedDocumentId = relatedDocumentId;
    }


    /**
     * Gets the version value for this RelatedDocumentDto.
     * 
     * @return version
     */
    public int getVersion() {
        return version;
    }


    /**
     * Sets the version value for this RelatedDocumentDto.
     * 
     * @param version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RelatedDocumentDto)) return false;
        RelatedDocumentDto other = (RelatedDocumentDto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.companyId == other.getCompanyId() &&
            this.documentId == other.getDocumentId() &&
            ((this.foo==null && other.getFoo()==null) || 
             (this.foo!=null &&
              java.util.Arrays.equals(this.foo, other.getFoo()))) &&
            this.relatedDocumentId == other.getRelatedDocumentId() &&
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
        _hashCode += getRelatedDocumentId();
        _hashCode += getVersion();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RelatedDocumentDto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "relatedDocumentDto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("relatedDocumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "relatedDocumentId"));
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
