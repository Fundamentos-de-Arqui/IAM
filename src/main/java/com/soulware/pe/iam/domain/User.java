package com.soulware.pe.iam.domain;

public class User {
    private final Long id;
    private final AccountType accountType;
    private final String passwordHash;
    private final String documentType;
    private final String identityDocumentNumber;
    private final Long profileId;

    public User(Long id, AccountType accountType, String passwordHash, 
                String documentType, String identityDocumentNumber, Long profileId) {
        this.id = id;
        this.accountType = accountType;
        this.passwordHash = passwordHash;
        this.documentType = documentType;
        this.identityDocumentNumber = identityDocumentNumber;
        this.profileId = profileId;
    }

    public Long getId() { return id; }
    public AccountType getAccountType() { return accountType; }
    public String getPasswordHash() { return passwordHash; }
    public String getDocumentType() { return documentType; }
    public String getIdentityDocumentNumber() { return identityDocumentNumber; }
    public Long getProfileId() { return profileId; }

    public enum AccountType {
        PATIENT, LEGAL_RESPONSIBLE, ADMIN, THERAPIST
    }
}
