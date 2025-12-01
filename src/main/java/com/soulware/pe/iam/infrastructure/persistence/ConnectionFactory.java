package com.soulware.pe.iam.infrastructure.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {
    
    private static final String DATABASE_URL = "jdbc:postgresql://ep-muddy-cell-ad61yuec-pooler.c-2.us-east-1.aws.neon.tech/neondb?user=neondb_owner&password=npg_pyqQdKnz4XR7&sslmode=require&channelBinding=require";
    
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Error connecting to PostgreSQL: " + e.getMessage());
            throw e;
        }
    }
    
    public void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Crear tabla si no existe
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "account_type VARCHAR(20) NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL, " +
                    "document_type VARCHAR(20) NOT NULL, " +
                    "identity_document_number VARCHAR(50) UNIQUE NOT NULL, " +
                    "profile_id BIGINT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            
            conn.createStatement().execute(createTableSQL);
            
            // Agregar columna profile_id si no existe (para tablas existentes)
            try {
                // Verificar si la columna existe
                String checkColumnSQL = "SELECT column_name FROM information_schema.columns " +
                        "WHERE table_name = 'users' AND column_name = 'profile_id'";
                ResultSet rs = conn.createStatement().executeQuery(checkColumnSQL);
                
                if (!rs.next()) {
                    // La columna no existe, agregarla
                    String alterTableSQL = "ALTER TABLE users ADD COLUMN profile_id BIGINT";
                    conn.createStatement().execute(alterTableSQL);
                    System.out.println("Column profile_id added successfully to existing table");
                } else {
                    System.out.println("Column profile_id already exists");
                }
            } catch (SQLException e) {
                System.err.println("Error checking/adding profile_id column: " + e.getMessage());
                // Continuar de todas formas - puede que la columna ya exista
            }
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
