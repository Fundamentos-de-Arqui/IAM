package com.soulware.pe.iam.interfaces.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponseDTO {
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("accountType")
    private String accountType;
    
    @JsonProperty("email")
    private String email;
    
    public RegisterResponseDTO() {}
    
    public RegisterResponseDTO(String userId, String accountType, String email) {
        this.userId = userId;
        this.accountType = accountType;
        this.email = email;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}
