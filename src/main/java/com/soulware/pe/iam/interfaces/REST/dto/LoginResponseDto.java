package com.soulware.pe.iam.interfaces.REST.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponseDto {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("accountType")
    private String accountType;
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("profileId")
    private Long profileId;
    
    public LoginResponseDto() {}
    
    public LoginResponseDto(Long id, String accountType, String token, String message, Long profileId) {
        this.id = id;
        this.accountType = accountType;
        this.token = token;
        this.message = message;
        this.profileId = profileId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAccountType() {
        return accountType;
    }
    
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Long getProfileId() {
        return profileId;
    }
    
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
