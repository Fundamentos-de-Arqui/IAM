package com.soulware.pe.iam.interfaces.REST.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {
    
    @JsonProperty("identityDocumentNumber")
    private String identityDocumentNumber;
    
    @JsonProperty("password")
    private String password;
    
    public LoginRequest() {}
    
    public LoginRequest(String identityDocumentNumber, String password) {
        this.identityDocumentNumber = identityDocumentNumber;
        this.password = password;
    }
    
    public String getIdentityDocumentNumber() {
        return identityDocumentNumber;
    }
    
    public void setIdentityDocumentNumber(String identityDocumentNumber) {
        this.identityDocumentNumber = identityDocumentNumber;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
