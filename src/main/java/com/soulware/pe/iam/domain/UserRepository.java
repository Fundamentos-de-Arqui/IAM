package com.soulware.pe.iam.domain;

public interface UserRepository {

    void save(User user) throws Exception;
    
    User saveAndReturn(User user) throws Exception;

    User findByIdentityDocumentNumber(String identityDocumentNumber) throws Exception;
}
