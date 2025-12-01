package com.soulware.pe.iam.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulware.pe.iam.application.RegisterUserService;
import com.soulware.pe.iam.domain.User;
import com.soulware.pe.iam.interfaces.messaging.dto.RegisterAccountMessage;
import com.soulware.pe.iam.interfaces.messaging.dto.RegisterResponseDTO;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class RegisterMessageListener implements MessageListener {
    
    private final RegisterUserService registerUserService;
    private final ActiveMQService activeMQService;
    private final ObjectMapper objectMapper;
    
    public RegisterMessageListener(RegisterUserService registerUserService, ActiveMQService activeMQService) {
        this.registerUserService = registerUserService;
        this.activeMQService = activeMQService;
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String jsonContent = textMessage.getText();
                
                System.out.println("Processing register message: " + jsonContent);
                
                RegisterAccountMessage registerMessage = objectMapper.readValue(jsonContent, RegisterAccountMessage.class);
                
                // Process the registration
                User.AccountType accountType = User.AccountType.valueOf(registerMessage.getAccountType());
                
                User registeredUser = registerUserService.registerFromMessage(
                    registerMessage.getPassword(),
                    accountType,
                    registerMessage.getDocumentType(),
                    registerMessage.getIdentityDocumentNumber(),
                    registerMessage.getProfileId()
                );
                
                // Send response back to API Gateway
                RegisterResponseDTO response = new RegisterResponseDTO(
                    registeredUser.getId().toString(),
                    registeredUser.getAccountType().toString(),
                    null // No email field in simplified User
                );
                
                activeMQService.sendRegisterResponse(response);
                
                System.out.println("Successfully processed registration for user: " + registeredUser.getId());
                
            }
        } catch (JMSException e) {
            System.err.println("Error processing JMS message: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error processing register message: " + e.getMessage());
            
            // Send error response
            try {
                RegisterResponseDTO errorResponse = new RegisterResponseDTO(
                    null,
                    null,
                    null
                );
                activeMQService.sendRegisterResponse(errorResponse);
            } catch (Exception sendError) {
                System.err.println("Error sending error response: " + sendError.getMessage());
            }
        }
    }
}
