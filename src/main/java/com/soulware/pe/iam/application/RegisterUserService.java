package com.soulware.pe.iam.application;

import com.soulware.pe.iam.domain.User;
import com.soulware.pe.iam.domain.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterUserService {

    private final UserRepository userRepository;

    public RegisterUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(String identityDocumentNumber, String plainPassword, User.AccountType accountType, String documentType) throws Exception {
        // basic validation
        if (identityDocumentNumber == null || identityDocumentNumber.isEmpty()) {
            throw new IllegalArgumentException("Identity document number required");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password too short");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type required");
        }
        if (documentType == null || documentType.isEmpty()) {
            throw new IllegalArgumentException("Document type required");
        }

        // check if user exists
        if (userRepository.findByIdentityDocumentNumber(identityDocumentNumber) != null) {
            throw new IllegalStateException("User already exists");
        }

        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User(null, accountType, hash, documentType, identityDocumentNumber);

        userRepository.save(user);
    }
    
    public User registerFromMessage(String plainPassword, User.AccountType accountType, 
                                   String documentType, String identityDocumentNumber) throws Exception {
        // basic validation
        if (identityDocumentNumber == null || identityDocumentNumber.isEmpty()) {
            throw new IllegalArgumentException("Identity document number required");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password too short");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type required");
        }
        if (documentType == null || documentType.isEmpty()) {
            throw new IllegalArgumentException("Document type required");
        }

        // check if user exists
        if (userRepository.findByIdentityDocumentNumber(identityDocumentNumber) != null) {
            throw new IllegalStateException("User already exists");
        }

        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User(null, accountType, hash, documentType, identityDocumentNumber);

        return userRepository.saveAndReturn(user);
    }
    
    public User registerWithDetails(String plainPassword, User.AccountType accountType, 
                                   String documentType, String identityDocumentNumber) throws Exception {
        // basic validation
        if (identityDocumentNumber == null || identityDocumentNumber.isEmpty()) {
            throw new IllegalArgumentException("Identity document number required");
        }
        if (plainPassword == null || plainPassword.length() < 6) {
            throw new IllegalArgumentException("Password too short");
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type required");
        }
        if (documentType == null || documentType.isEmpty()) {
            throw new IllegalArgumentException("Document type required");
        }

        // check if user exists
        if (userRepository.findByIdentityDocumentNumber(identityDocumentNumber) != null) {
            throw new IllegalStateException("User already exists");
        }

        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        User user = new User(null, accountType, hash, documentType, identityDocumentNumber);

        return userRepository.saveAndReturn(user);
    }
}
