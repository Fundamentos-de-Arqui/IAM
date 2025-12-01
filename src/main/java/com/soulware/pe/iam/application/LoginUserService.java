package com.soulware.pe.iam.application;

import com.soulware.pe.iam.domain.User;
import com.soulware.pe.iam.domain.UserRepository;
import com.soulware.pe.iam.infrastructure.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

public class LoginUserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    
    public LoginUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = new JwtUtil();
    }

    public LoginResult login(String identityDocumentNumber, String plainPassword) {
        try {
            if (identityDocumentNumber == null || identityDocumentNumber.isEmpty()) {
                throw new IllegalArgumentException("Identity document number required");
            }

            if (plainPassword == null || plainPassword.isEmpty()) {
                throw new IllegalArgumentException("Password required");
            }

            User user = userRepository.findByIdentityDocumentNumber(identityDocumentNumber);
            
            if (user == null) {
                return new LoginResult(false, null, null, null, null, "Invalid credentials");
            }
            
            if (!BCrypt.checkpw(plainPassword, user.getPasswordHash())) {
                return new LoginResult(false, null, null, null, null, "Invalid credentials");
            }
            
            String token = jwtUtil.generateToken(user.getIdentityDocumentNumber(), user.getId());
            return new LoginResult(true, user.getId(), user.getAccountType().toString(), token, user.getProfileId(), "Login successful");
            
        } catch (Exception e) {
            return new LoginResult(false, null, null, null, null, "Login failed: " + e.getMessage());
        }
    }
    
    public static class LoginResult {
        private final boolean success;
        private final Long userId;
        private final String accountType;
        private final String token;
        private final Long profileId;
        private final String message;
        
        public LoginResult(boolean success, Long userId, String accountType, String token, Long profileId, String message) {
            this.success = success;
            this.userId = userId;
            this.accountType = accountType;
            this.token = token;
            this.profileId = profileId;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public Long getUserId() { return userId; }
        public String getAccountType() { return accountType; }
        public String getToken() { return token; }
        public Long getProfileId() { return profileId; }
        public String getMessage() { return message; }
    }
}
