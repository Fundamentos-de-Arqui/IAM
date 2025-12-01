package com.soulware.pe.iam.infrastructure.persistence;

import com.soulware.pe.iam.domain.User;
import com.soulware.pe.iam.domain.UserRepository;

import java.sql.*;

public class PostgresUserRepository implements UserRepository {

    private final ConnectionFactory connectionFactory;

    public PostgresUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void save(User user) throws Exception {
        String sql = "INSERT INTO users (account_type, password_hash, document_type, identity_document_number, profile_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAccountType().toString());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getDocumentType());
            ps.setString(4, user.getIdentityDocumentNumber());
            if (user.getProfileId() != null) {
                ps.setLong(5, user.getProfileId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error saving user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User saveAndReturn(User user) throws Exception {
        String sql = "INSERT INTO users (account_type, password_hash, document_type, identity_document_number, profile_id) VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getAccountType().toString());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getDocumentType());
            ps.setString(4, user.getIdentityDocumentNumber());
            if (user.getProfileId() != null) {
                ps.setLong(5, user.getProfileId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    return new User(id, user.getAccountType(), user.getPasswordHash(), 
                                  user.getDocumentType(), user.getIdentityDocumentNumber(), user.getProfileId());
                }
                throw new Exception("Failed to retrieve generated user ID");
            }
        } catch (SQLException e) {
            throw new Exception("Error saving user: " + e.getMessage(), e);
        }
    }

    @Override
    public User findByIdentityDocumentNumber(String identityDocumentNumber) throws Exception {
        String sql = "SELECT id, account_type, password_hash, document_type, identity_document_number, profile_id FROM users WHERE identity_document_number = ?";

        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, identityDocumentNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    String accountTypeStr = rs.getString("account_type");
                    String hash = rs.getString("password_hash");
                    String documentType = rs.getString("document_type");
                    String docNumber = rs.getString("identity_document_number");
                    Long profileId = rs.getLong("profile_id");
                    if (rs.wasNull()) {
                        profileId = null;
                    }
                    
                    User.AccountType accountType = User.AccountType.valueOf(accountTypeStr);
                    
                    return new User(id, accountType, hash, documentType, docNumber, profileId);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new Exception("Error finding user: " + e.getMessage(), e);
        }
    }
}
