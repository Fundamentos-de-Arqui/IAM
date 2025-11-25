package com.soulware.pe.iam.interfaces.REST.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("identityDocumentNumber")
    private String identityDocumentNumber;
    
    public UserInfoResponse() {}
    
    public UserInfoResponse(Long id, String identityDocumentNumber) {
        this.id = id;
        this.identityDocumentNumber = identityDocumentNumber;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdentityDocumentNumber() {
        return identityDocumentNumber;
    }
    
    public void setIdentityDocumentNumber(String identityDocumentNumber) {
        this.identityDocumentNumber = identityDocumentNumber;
    }
}
