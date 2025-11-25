package com.soulware.pe.iam.interfaces.REST.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestExtended {
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("accountType")
    private String accountType;
    
    @JsonProperty("documentType")
    private String documentType;
    
    @JsonProperty("identityDocumentNumber")
    private String identityDocumentNumber;
    
    public RegisterRequestExtended() {}
    
    public RegisterRequestExtended(String password, String accountType, 
                                 String documentType, String identityDocumentNumber) {
        this.password = password;
        this.accountType = accountType;
        this.documentType = documentType;
        this.identityDocumentNumber = identityDocumentNumber;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    
    public String getIdentityDocumentNumber() {
        return identityDocumentNumber;
    }
    
    public void setIdentityDocumentNumber(String identityDocumentNumber) {
        this.identityDocumentNumber = identityDocumentNumber;
    }
}
