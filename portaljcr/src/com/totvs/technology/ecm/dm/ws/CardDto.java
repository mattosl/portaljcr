/**
 * CardDto.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.totvs.technology.ecm.dm.ws;

public class CardDto  implements java.io.Serializable {
    private java.lang.String additionalComments;

    private com.totvs.technology.ecm.dm.ws.Attachment[] attachs;

    private com.totvs.technology.ecm.dm.ws.CardFieldDto[] cardData;

    private java.lang.String colleagueId;

    private com.totvs.technology.ecm.dm.ws.ApproverDto[] docapprovers;

    private com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] docsecurity;

    private java.lang.String documentDescription;

    private java.lang.Integer documentId;

    private java.lang.String documentKeyWord;

    private java.util.Calendar expirationDate;

    private java.lang.Boolean expires;

    private boolean inheritSecurity;

    private java.lang.Integer parentDocumentId;

    private com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] reldocs;

    private java.lang.Integer topicId;

    private java.lang.Boolean userNotify;

    private java.util.Calendar validationStartDate;

    private java.lang.Integer version;

    private java.lang.String versionDescription;

    public CardDto() {
    }

    public CardDto(
           java.lang.String additionalComments,
           com.totvs.technology.ecm.dm.ws.Attachment[] attachs,
           com.totvs.technology.ecm.dm.ws.CardFieldDto[] cardData,
           java.lang.String colleagueId,
           com.totvs.technology.ecm.dm.ws.ApproverDto[] docapprovers,
           com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] docsecurity,
           java.lang.String documentDescription,
           java.lang.Integer documentId,
           java.lang.String documentKeyWord,
           java.util.Calendar expirationDate,
           java.lang.Boolean expires,
           boolean inheritSecurity,
           java.lang.Integer parentDocumentId,
           com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] reldocs,
           java.lang.Integer topicId,
           java.lang.Boolean userNotify,
           java.util.Calendar validationStartDate,
           java.lang.Integer version,
           java.lang.String versionDescription) {
           this.additionalComments = additionalComments;
           this.attachs = attachs;
           this.cardData = cardData;
           this.colleagueId = colleagueId;
           this.docapprovers = docapprovers;
           this.docsecurity = docsecurity;
           this.documentDescription = documentDescription;
           this.documentId = documentId;
           this.documentKeyWord = documentKeyWord;
           this.expirationDate = expirationDate;
           this.expires = expires;
           this.inheritSecurity = inheritSecurity;
           this.parentDocumentId = parentDocumentId;
           this.reldocs = reldocs;
           this.topicId = topicId;
           this.userNotify = userNotify;
           this.validationStartDate = validationStartDate;
           this.version = version;
           this.versionDescription = versionDescription;
    }


    /**
     * Gets the additionalComments value for this CardDto.
     * 
     * @return additionalComments
     */
    public java.lang.String getAdditionalComments() {
        return additionalComments;
    }


    /**
     * Sets the additionalComments value for this CardDto.
     * 
     * @param additionalComments
     */
    public void setAdditionalComments(java.lang.String additionalComments) {
        this.additionalComments = additionalComments;
    }


    /**
     * Gets the attachs value for this CardDto.
     * 
     * @return attachs
     */
    public com.totvs.technology.ecm.dm.ws.Attachment[] getAttachs() {
        return attachs;
    }


    /**
     * Sets the attachs value for this CardDto.
     * 
     * @param attachs
     */
    public void setAttachs(com.totvs.technology.ecm.dm.ws.Attachment[] attachs) {
        this.attachs = attachs;
    }

    public com.totvs.technology.ecm.dm.ws.Attachment getAttachs(int i) {
        return this.attachs[i];
    }

    public void setAttachs(int i, com.totvs.technology.ecm.dm.ws.Attachment _value) {
        this.attachs[i] = _value;
    }


    /**
     * Gets the cardData value for this CardDto.
     * 
     * @return cardData
     */
    public com.totvs.technology.ecm.dm.ws.CardFieldDto[] getCardData() {
        return cardData;
    }


    /**
     * Sets the cardData value for this CardDto.
     * 
     * @param cardData
     */
    public void setCardData(com.totvs.technology.ecm.dm.ws.CardFieldDto[] cardData) {
        this.cardData = cardData;
    }

    public com.totvs.technology.ecm.dm.ws.CardFieldDto getCardData(int i) {
        return this.cardData[i];
    }

    public void setCardData(int i, com.totvs.technology.ecm.dm.ws.CardFieldDto _value) {
        this.cardData[i] = _value;
    }


    /**
     * Gets the colleagueId value for this CardDto.
     * 
     * @return colleagueId
     */
    public java.lang.String getColleagueId() {
        return colleagueId;
    }


    /**
     * Sets the colleagueId value for this CardDto.
     * 
     * @param colleagueId
     */
    public void setColleagueId(java.lang.String colleagueId) {
        this.colleagueId = colleagueId;
    }


    /**
     * Gets the docapprovers value for this CardDto.
     * 
     * @return docapprovers
     */
    public com.totvs.technology.ecm.dm.ws.ApproverDto[] getDocapprovers() {
        return docapprovers;
    }


    /**
     * Sets the docapprovers value for this CardDto.
     * 
     * @param docapprovers
     */
    public void setDocapprovers(com.totvs.technology.ecm.dm.ws.ApproverDto[] docapprovers) {
        this.docapprovers = docapprovers;
    }

    public com.totvs.technology.ecm.dm.ws.ApproverDto getDocapprovers(int i) {
        return this.docapprovers[i];
    }

    public void setDocapprovers(int i, com.totvs.technology.ecm.dm.ws.ApproverDto _value) {
        this.docapprovers[i] = _value;
    }


    /**
     * Gets the docsecurity value for this CardDto.
     * 
     * @return docsecurity
     */
    public com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] getDocsecurity() {
        return docsecurity;
    }


    /**
     * Sets the docsecurity value for this CardDto.
     * 
     * @param docsecurity
     */
    public void setDocsecurity(com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto[] docsecurity) {
        this.docsecurity = docsecurity;
    }

    public com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto getDocsecurity(int i) {
        return this.docsecurity[i];
    }

    public void setDocsecurity(int i, com.totvs.technology.ecm.dm.ws.DocumentSecurityConfigDto _value) {
        this.docsecurity[i] = _value;
    }


    /**
     * Gets the documentDescription value for this CardDto.
     * 
     * @return documentDescription
     */
    public java.lang.String getDocumentDescription() {
        return documentDescription;
    }


    /**
     * Sets the documentDescription value for this CardDto.
     * 
     * @param documentDescription
     */
    public void setDocumentDescription(java.lang.String documentDescription) {
        this.documentDescription = documentDescription;
    }


    /**
     * Gets the documentId value for this CardDto.
     * 
     * @return documentId
     */
    public java.lang.Integer getDocumentId() {
        return documentId;
    }


    /**
     * Sets the documentId value for this CardDto.
     * 
     * @param documentId
     */
    public void setDocumentId(java.lang.Integer documentId) {
        this.documentId = documentId;
    }


    /**
     * Gets the documentKeyWord value for this CardDto.
     * 
     * @return documentKeyWord
     */
    public java.lang.String getDocumentKeyWord() {
        return documentKeyWord;
    }


    /**
     * Sets the documentKeyWord value for this CardDto.
     * 
     * @param documentKeyWord
     */
    public void setDocumentKeyWord(java.lang.String documentKeyWord) {
        this.documentKeyWord = documentKeyWord;
    }


    /**
     * Gets the expirationDate value for this CardDto.
     * 
     * @return expirationDate
     */
    public java.util.Calendar getExpirationDate() {
        return expirationDate;
    }


    /**
     * Sets the expirationDate value for this CardDto.
     * 
     * @param expirationDate
     */
    public void setExpirationDate(java.util.Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }


    /**
     * Gets the expires value for this CardDto.
     * 
     * @return expires
     */
    public java.lang.Boolean getExpires() {
        return expires;
    }


    /**
     * Sets the expires value for this CardDto.
     * 
     * @param expires
     */
    public void setExpires(java.lang.Boolean expires) {
        this.expires = expires;
    }


    /**
     * Gets the inheritSecurity value for this CardDto.
     * 
     * @return inheritSecurity
     */
    public boolean isInheritSecurity() {
        return inheritSecurity;
    }


    /**
     * Sets the inheritSecurity value for this CardDto.
     * 
     * @param inheritSecurity
     */
    public void setInheritSecurity(boolean inheritSecurity) {
        this.inheritSecurity = inheritSecurity;
    }


    /**
     * Gets the parentDocumentId value for this CardDto.
     * 
     * @return parentDocumentId
     */
    public java.lang.Integer getParentDocumentId() {
        return parentDocumentId;
    }


    /**
     * Sets the parentDocumentId value for this CardDto.
     * 
     * @param parentDocumentId
     */
    public void setParentDocumentId(java.lang.Integer parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }


    /**
     * Gets the reldocs value for this CardDto.
     * 
     * @return reldocs
     */
    public com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] getReldocs() {
        return reldocs;
    }


    /**
     * Sets the reldocs value for this CardDto.
     * 
     * @param reldocs
     */
    public void setReldocs(com.totvs.technology.ecm.dm.ws.RelatedDocumentDto[] reldocs) {
        this.reldocs = reldocs;
    }

    public com.totvs.technology.ecm.dm.ws.RelatedDocumentDto getReldocs(int i) {
        return this.reldocs[i];
    }

    public void setReldocs(int i, com.totvs.technology.ecm.dm.ws.RelatedDocumentDto _value) {
        this.reldocs[i] = _value;
    }


    /**
     * Gets the topicId value for this CardDto.
     * 
     * @return topicId
     */
    public java.lang.Integer getTopicId() {
        return topicId;
    }


    /**
     * Sets the topicId value for this CardDto.
     * 
     * @param topicId
     */
    public void setTopicId(java.lang.Integer topicId) {
        this.topicId = topicId;
    }


    /**
     * Gets the userNotify value for this CardDto.
     * 
     * @return userNotify
     */
    public java.lang.Boolean getUserNotify() {
        return userNotify;
    }


    /**
     * Sets the userNotify value for this CardDto.
     * 
     * @param userNotify
     */
    public void setUserNotify(java.lang.Boolean userNotify) {
        this.userNotify = userNotify;
    }


    /**
     * Gets the validationStartDate value for this CardDto.
     * 
     * @return validationStartDate
     */
    public java.util.Calendar getValidationStartDate() {
        return validationStartDate;
    }


    /**
     * Sets the validationStartDate value for this CardDto.
     * 
     * @param validationStartDate
     */
    public void setValidationStartDate(java.util.Calendar validationStartDate) {
        this.validationStartDate = validationStartDate;
    }


    /**
     * Gets the version value for this CardDto.
     * 
     * @return version
     */
    public java.lang.Integer getVersion() {
        return version;
    }


    /**
     * Sets the version value for this CardDto.
     * 
     * @param version
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }


    /**
     * Gets the versionDescription value for this CardDto.
     * 
     * @return versionDescription
     */
    public java.lang.String getVersionDescription() {
        return versionDescription;
    }


    /**
     * Sets the versionDescription value for this CardDto.
     * 
     * @param versionDescription
     */
    public void setVersionDescription(java.lang.String versionDescription) {
        this.versionDescription = versionDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CardDto)) return false;
        CardDto other = (CardDto) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.additionalComments==null && other.getAdditionalComments()==null) || 
             (this.additionalComments!=null &&
              this.additionalComments.equals(other.getAdditionalComments()))) &&
            ((this.attachs==null && other.getAttachs()==null) || 
             (this.attachs!=null &&
              java.util.Arrays.equals(this.attachs, other.getAttachs()))) &&
            ((this.cardData==null && other.getCardData()==null) || 
             (this.cardData!=null &&
              java.util.Arrays.equals(this.cardData, other.getCardData()))) &&
            ((this.colleagueId==null && other.getColleagueId()==null) || 
             (this.colleagueId!=null &&
              this.colleagueId.equals(other.getColleagueId()))) &&
            ((this.docapprovers==null && other.getDocapprovers()==null) || 
             (this.docapprovers!=null &&
              java.util.Arrays.equals(this.docapprovers, other.getDocapprovers()))) &&
            ((this.docsecurity==null && other.getDocsecurity()==null) || 
             (this.docsecurity!=null &&
              java.util.Arrays.equals(this.docsecurity, other.getDocsecurity()))) &&
            ((this.documentDescription==null && other.getDocumentDescription()==null) || 
             (this.documentDescription!=null &&
              this.documentDescription.equals(other.getDocumentDescription()))) &&
            ((this.documentId==null && other.getDocumentId()==null) || 
             (this.documentId!=null &&
              this.documentId.equals(other.getDocumentId()))) &&
            ((this.documentKeyWord==null && other.getDocumentKeyWord()==null) || 
             (this.documentKeyWord!=null &&
              this.documentKeyWord.equals(other.getDocumentKeyWord()))) &&
            ((this.expirationDate==null && other.getExpirationDate()==null) || 
             (this.expirationDate!=null &&
              this.expirationDate.equals(other.getExpirationDate()))) &&
            ((this.expires==null && other.getExpires()==null) || 
             (this.expires!=null &&
              this.expires.equals(other.getExpires()))) &&
            this.inheritSecurity == other.isInheritSecurity() &&
            ((this.parentDocumentId==null && other.getParentDocumentId()==null) || 
             (this.parentDocumentId!=null &&
              this.parentDocumentId.equals(other.getParentDocumentId()))) &&
            ((this.reldocs==null && other.getReldocs()==null) || 
             (this.reldocs!=null &&
              java.util.Arrays.equals(this.reldocs, other.getReldocs()))) &&
            ((this.topicId==null && other.getTopicId()==null) || 
             (this.topicId!=null &&
              this.topicId.equals(other.getTopicId()))) &&
            ((this.userNotify==null && other.getUserNotify()==null) || 
             (this.userNotify!=null &&
              this.userNotify.equals(other.getUserNotify()))) &&
            ((this.validationStartDate==null && other.getValidationStartDate()==null) || 
             (this.validationStartDate!=null &&
              this.validationStartDate.equals(other.getValidationStartDate()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.versionDescription==null && other.getVersionDescription()==null) || 
             (this.versionDescription!=null &&
              this.versionDescription.equals(other.getVersionDescription())));
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
        if (getAdditionalComments() != null) {
            _hashCode += getAdditionalComments().hashCode();
        }
        if (getAttachs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttachs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttachs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCardData() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCardData());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCardData(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getColleagueId() != null) {
            _hashCode += getColleagueId().hashCode();
        }
        if (getDocapprovers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocapprovers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocapprovers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDocsecurity() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocsecurity());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocsecurity(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getDocumentDescription() != null) {
            _hashCode += getDocumentDescription().hashCode();
        }
        if (getDocumentId() != null) {
            _hashCode += getDocumentId().hashCode();
        }
        if (getDocumentKeyWord() != null) {
            _hashCode += getDocumentKeyWord().hashCode();
        }
        if (getExpirationDate() != null) {
            _hashCode += getExpirationDate().hashCode();
        }
        if (getExpires() != null) {
            _hashCode += getExpires().hashCode();
        }
        _hashCode += (isInheritSecurity() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getParentDocumentId() != null) {
            _hashCode += getParentDocumentId().hashCode();
        }
        if (getReldocs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReldocs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReldocs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTopicId() != null) {
            _hashCode += getTopicId().hashCode();
        }
        if (getUserNotify() != null) {
            _hashCode += getUserNotify().hashCode();
        }
        if (getValidationStartDate() != null) {
            _hashCode += getValidationStartDate().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getVersionDescription() != null) {
            _hashCode += getVersionDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CardDto.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "cardDto"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalComments");
        elemField.setXmlName(new javax.xml.namespace.QName("", "additionalComments"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attachs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "attachs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "attachment"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cardData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "cardFieldDto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colleagueId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "colleagueId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docapprovers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "docapprovers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "approverDto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docsecurity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "docsecurity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "documentSecurityConfigDto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentKeyWord");
        elemField.setXmlName(new javax.xml.namespace.QName("", "documentKeyWord"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expirationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expires");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expires"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inheritSecurity");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inheritSecurity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parentDocumentId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parentDocumentId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reldocs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reldocs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ws.dm.ecm.technology.totvs.com/", "relatedDocumentDto"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("topicId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "topicId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userNotify");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userNotify"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validationStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validationStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("versionDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("", "versionDescription"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
