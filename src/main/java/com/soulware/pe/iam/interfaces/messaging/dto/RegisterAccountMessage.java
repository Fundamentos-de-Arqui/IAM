package com.soulware.pe.iam.interfaces.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterAccountMessage {
    
    @JsonProperty("accountType")
    private String accountType;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("documentType")
    private String documentType;
    
    @JsonProperty("identityDocumentNumber")
    private String identityDocumentNumber;

    @JsonProperty("profileId")
    private Long profileId;
    
    public RegisterAccountMessage() {}
    
    public RegisterAccountMessage(String accountType, String password, String documentType, String identityDocumentNumber, Long profileId) {
        this.accountType = accountType;
        this.password = password;
        this.documentType = documentType;
        this.identityDocumentNumber = identityDocumentNumber;
        this.profileId = profileId;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
    
    public void setIdentityDocumentNumber(String identityDocumentNumber) {
        this.identityDocumentNumber = identityDocumentNumber;
    }
}
