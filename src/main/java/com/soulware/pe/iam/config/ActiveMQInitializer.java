package com.soulware.pe.iam.config;

import com.soulware.pe.iam.application.RegisterUserService;
import com.soulware.pe.iam.domain.UserRepository;
import com.soulware.pe.iam.infrastructure.messaging.ActiveMQService;
import com.soulware.pe.iam.infrastructure.messaging.RegisterMessageListener;
import com.soulware.pe.iam.infrastructure.persistence.ConnectionFactory;
import com.soulware.pe.iam.infrastructure.persistence.PostgresUserRepository;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ActiveMQInitializer implements ServletContextListener {
    
    private ActiveMQService activeMQService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            System.out.println("Initializing ActiveMQ message listener...");
            
            // Initialize dependencies
            ConnectionFactory connectionFactory = new ConnectionFactory();
            UserRepository userRepository = new PostgresUserRepository(connectionFactory);
            RegisterUserService registerUserService = new RegisterUserService(userRepository);
            
            // Initialize ActiveMQ service
            activeMQService = new ActiveMQService();
            
            // Create and start message listener
            RegisterMessageListener messageListener = new RegisterMessageListener(registerUserService, activeMQService);
            activeMQService.startMessageListener(messageListener);
            
            System.out.println("ActiveMQ message listener initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize ActiveMQ: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down ActiveMQ connections...");
        // Cleanup is handled by shutdown hooks in ActiveMQService
    }
}
